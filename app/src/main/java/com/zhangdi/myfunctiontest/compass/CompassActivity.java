package com.zhangdi.myfunctiontest.compass;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.zhangdi.myfunctiontest.R;

import java.util.Arrays;
import java.util.Locale;

public class CompassActivity extends Activity {

    private final float MAX_ROATE_DEGREE = 1.0f;
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    private LocationManager mLocationManager;
    private String mLocationProvider;
    private float mDirection;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing;
    private boolean mChinease;
    private Context mContext;

    View mCompassView;
    CompassView mPointer;
    TextView mLocationTextView;
    LinearLayout mDirectionLayout;
    LinearLayout mAngleLayout;

    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {
                    Log.d("TAG", "run: mTargetDirection = "+mTargetDirection+";mDirection = "+mDirection);
                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    Log.d("TAG", "run: "+Math.abs(distance));
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    mPointer.updateDirection(mDirection);
                }

                updateDirection();

                mHandler.postDelayed(mCompassViewUpdater, 20);
            }
        }
    };

    private Sensor magneticSensor = null;
    private Sensor accelerometerSensor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        initResources();
        initServices();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null) { // 初始化两个传感器
            // getDefaultSensor:获取Sensor,使用给定的类型和唤醒属性返回传感器。
            magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (magneticSensor != null) {
            mSensorManager.registerListener(mSensorListener, magneticSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }

        Log.d("TAG", "onResume: accelerometerSensor = "+accelerometerSensor);
        if (accelerometerSensor != null) {
            mSensorManager.registerListener(mSensorListener, accelerometerSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }

    }
    private float[] geomagnetic = new float[3];
    private float[] gravity = new float[3];
    private SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.e("TAG", "onSensorChanged 开始");
            // SensorEvent:保存精度(accuracy)、传感器类型(sensor)、时间戳(timestamp)
            // 以及不同传感器(Sensor)具有的不同传感器数组(values)。


            // TYPE_MAGNETIC_FIELD:描述磁场传感器类型的常量。
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic = event.values;
                Log.e("TAG", "onSensorChanged 得到磁场传感器: " + Arrays.toString(geomagnetic));
            }

            // TYPE_ACCELEROMETER:描述加速度传感器类型的常量。
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values;
                Log.e("TAG", "onSensorChanged 得到加速度传感器: " + Arrays.toString(gravity));
            }
            getValue(geomagnetic,gravity);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void getValue(float[] geomagnetic,float[] gravity){
        //初始化数组
        float[] values = new float[3]; // 用来保存手机的旋转弧度
        float[] r = new float[9]; // 被填充的旋转矩阵

        // 传入gravity和geomagnetic，通过计算它们得到旋转矩阵R。
        // 而第二个参数倾斜矩阵I是用于将磁场数据转换进实际的重力坐标系中的，一般默认设置为NULL即可。
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);

        // 根据旋转矩阵R计算设备的方向，将结果存储在values中。
        // values[0]记录着手机围绕 Z 轴的旋转弧度，
        // values[1]记录着手机围绕 X 轴的旋转弧度，
        // values[2]记录着手机围绕 Y 轴的旋转弧度。
        SensorManager.getOrientation(r, values);
        Log.e("TAG", "getValue R: " + Arrays.toString(r));
        Log.e("TAG", "getValue values: " + Arrays.toString(values));

        // 旋转弧度转为角度
        float pitch = (float) Math.toDegrees(values[0])*-1.0f;

        long time = System.currentTimeMillis();
//        Log.d("TAG", "getValue: time-lastChangeTime = "+(time-lastChangeTime));
//        if (Math.abs(pitch) > 1.0 && time-lastChangeTime>150) { // 设置条件防止频繁回调
//            Log.e(TAG, "getValue pitch: "+ pitch);
//            lastChangeTime = time;
//            change(pitch);
//        }
//        lastX = pitch;


        Log.d("TAG", "onSensorChanged: direction "+pitch);
        mTargetDirection = normalizeDegree(pitch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationProvider != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            updateLocation(mLocationManager.getLastKnownLocation(mLocationProvider));
            mLocationManager.requestLocationUpdates(mLocationProvider, 2000, 10, mLocationListener);
        } else {
            mLocationTextView.setText(R.string.cannot_get_location);
        }
        if (mOrientationSensor != null) {
//            mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
//                    SensorManager.SENSOR_DELAY_GAME);
        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 20);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStopDrawing = true;
        if (mOrientationSensor != null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        if (mLocationProvider != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    private void initResources() {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;
        mChinease = TextUtils.equals(Locale.getDefault().getLanguage(), "zh");

        mCompassView = findViewById(R.id.view_compass);
        mPointer = (CompassView) findViewById(R.id.compass_pointer);
        mLocationTextView = (TextView) findViewById(R.id.textview_location);
        mDirectionLayout = (LinearLayout) findViewById(R.id.layout_direction);
        mAngleLayout = (LinearLayout) findViewById(R.id.layout_angle);

        mPointer.setImageResource(mChinease ? R.drawable.compass_cn : R.drawable.compass);
    }

    private void initServices() {
        // sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mLocationProvider = mLocationManager.getBestProvider(criteria, true);

    }

    private void updateDirection() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mDirectionLayout.removeAllViews();
        mAngleLayout.removeAllViews();

        ImageView east = null;
        ImageView west = null;
        ImageView south = null;
        ImageView north = null;
        float direction = normalizeDegree(mTargetDirection * -1.0f);
        if (direction > 22.5f && direction < 157.5f) {
            // east
            east = new ImageView(this);
            east.setImageResource(mChinease ? R.drawable.e_cn : R.drawable.e);
            east.setLayoutParams(lp);
        } else if (direction > 202.5f && direction < 337.5f) {
            // west
            west = new ImageView(this);
            west.setImageResource(mChinease ? R.drawable.w_cn : R.drawable.w);
            west.setLayoutParams(lp);
        }

        if (direction > 112.5f && direction < 247.5f) {
            // south
            south = new ImageView(this);
            south.setImageResource(mChinease ? R.drawable.s_cn : R.drawable.s);
            south.setLayoutParams(lp);
        } else if (direction < 67.5 || direction > 292.5f) {
            // north
            north = new ImageView(this);
            north.setImageResource(mChinease ? R.drawable.n_cn : R.drawable.n);
            north.setLayoutParams(lp);
        }

        if (mChinease) {
            // east/west should be before north/south
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
        } else {
            // north/south should be before east/west
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
        }

        int direction2 = (int) direction;
        boolean show = false;
        if (direction2 >= 100) {
            mAngleLayout.addView(getNumberImage(direction2 / 100));
            direction2 %= 100;
            show = true;
        }
        if (direction2 >= 10 || show) {
            mAngleLayout.addView(getNumberImage(direction2 / 10));
            direction2 %= 10;
        }
        mAngleLayout.addView(getNumberImage(direction2));

        ImageView degreeImageView = new ImageView(this);
        degreeImageView.setImageResource(R.drawable.degree);
        degreeImageView.setLayoutParams(lp);
        mAngleLayout.addView(degreeImageView);
    }

    private ImageView getNumberImage(int number) {
        ImageView image = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        switch (number) {
            case 0:
                image.setImageResource(R.drawable.number_0);
                break;
            case 1:
                image.setImageResource(R.drawable.number_1);
                break;
            case 2:
                image.setImageResource(R.drawable.number_2);
                break;
            case 3:
                image.setImageResource(R.drawable.number_3);
                break;
            case 4:
                image.setImageResource(R.drawable.number_4);
                break;
            case 5:
                image.setImageResource(R.drawable.number_5);
                break;
            case 6:
                image.setImageResource(R.drawable.number_6);
                break;
            case 7:
                image.setImageResource(R.drawable.number_7);
                break;
            case 8:
                image.setImageResource(R.drawable.number_8);
                break;
            case 9:
                image.setImageResource(R.drawable.number_9);
                break;
        }
        image.setLayoutParams(lp);
        return image;
    }

    private void updateLocation(Location location) {
        if (location == null) {
            mLocationTextView.setText(R.string.getting_location);
        } else {
            StringBuilder sb = new StringBuilder();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if (latitude >= 0.0f) {
                sb.append(getString(R.string.location_north, getLocationString(latitude)));
            } else {
                sb.append(getString(R.string.location_south, getLocationString(-1.0 * latitude)));
            }

            sb.append("    ");

            if (longitude >= 0.0f) {
                sb.append(getString(R.string.location_east, getLocationString(longitude)));
            } else {
                sb.append(getString(R.string.location_west, getLocationString(-1.0 * longitude)));
            }

            mLocationTextView.setText(sb.toString());
        }
    }

    private String getLocationString(double input) {
        int du = (int) input;
        int fen = (((int) ((input - du) * 3600))) / 60;
        int miao = (((int) ((input - du) * 3600))) % 60;
        return String.valueOf(du) + "°" + String.valueOf(fen) + "'" + String.valueOf(miao) + "\"";
    }

    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            Log.d("TAG", "onSensorChanged: direction "+direction);
            mTargetDirection = normalizeDegree(direction);
            Log.d("TAG", "onSensorChanged: "+mTargetDirection);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status != LocationProvider.OUT_OF_SERVICE) {
                if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                updateLocation(mLocationManager.getLastKnownLocation(mLocationProvider));
            } else {
                mLocationTextView.setText(R.string.cannot_get_location);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

    };
}
