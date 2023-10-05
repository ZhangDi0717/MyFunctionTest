package com.zhangdi.myfunctiontest.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangdi.myfunctiontest.MiCompass;
import com.zhangdi.myfunctiontest.R;

import java.util.Arrays;

public class Compass3Activity extends AppCompatActivity {
    public static final int SENSOR_STATUS_NO_CONTACT = -1;
    public static final int SENSOR_STATUS_UNRELIABLE = 0;
    public static final int SENSOR_STATUS_ACCURACY_LOW = 1;
    /**
     * This sensor is reporting data with an average level of accuracy,
     * calibration with the environment may improve the readings
     */
    public static final int SENSOR_STATUS_ACCURACY_MEDIUM = 2;
    /** This sensor is reporting data with maximum accuracy */
    public static final int SENSOR_STATUS_ACCURACY_HIGH = 3;

    private SensorManager mSensorManager;
    private MySensorEventListener mMySensorEventListener;
    private MiCompass miui;
    private  float val;
    private float[] mAccelerometerReading = new float[3];
    private float[] mMagneticFieldReading = new float[3];
    private TextView mAccelerometerSensorTextView;
    private TextView mMagneticSensorTextView;
    private TextView mGyroscopeSensorTextView;
    private TextView mOrientationSensorTextView;
    private TextView mAdjustTextView;
    private String Tag = "Tag:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass3);
        mAccelerometerSensorTextView = findViewById(R.id.accSensor_tv);
        mMagneticSensorTextView = findViewById(R.id.mangeticSensor_tv);
        mGyroscopeSensorTextView = findViewById(R.id.gyroscopeSensor_tv);
        mOrientationSensorTextView = findViewById(R.id.orientationSensor_tv);
        mAdjustTextView = findViewById(R.id.adjust_tv);
        miui = findViewById(R.id.miui);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.mMySensorEventListener = new MySensorEventListener();

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor gyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (accelerometerSensor != null) {
            System.out.println("传感器存在");
            Log.d(Tag, "传感器存在");
        } else {
            System.out.println("传感器不存在");
            Log.d(Tag, "传感器不存在");
        }
        mSensorManager.registerListener(mMySensorEventListener,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        mSensorManager.registerListener(mMySensorEventListener,accelerometerSensor,
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(mMySensorEventListener,magneticSensor,
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(mMySensorEventListener,gyroscopeSensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mMySensorEventListener);
    }

    private void calculateOrientation() {
        final float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerReading, mMagneticFieldReading);

        final float[] orientationAngles = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        double v = Math.toDegrees(orientationAngles[0]);
        if (v < 0){
            v = 360+v;
        }
        miui.setVal((float) v);
        System.out.println("orientation data[x:" + v+ ", y:" + orientationAngles[1] + ", z:" + orientationAngles[2] + "]");
        mOrientationSensorTextView.setText("orientation [x:" + orientationAngles[0] + ", y:" + orientationAngles[1] + ", z:" + orientationAngles[2] + "]");
    }

    //加速度传感器数据
    float accelerometerValues[]=new float[3];
    //地磁传感器数据
    float magneticValues[]=new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[]=new float[9];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[]=new float[3];

    int compassNumber;
    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {

            final float alpha = 0.97f;
            // 判断当前是加速度感应器还是地磁感应器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //赋值调用clone方法
//            accelerometerValues = event.values.clone();
                accelerometerValues[0]=alpha * accelerometerValues[0] + (1 - alpha) * event.values[0];
                accelerometerValues[1]=alpha * accelerometerValues[1] + (1 - alpha) * event.values[1];
                accelerometerValues[2]=alpha * accelerometerValues[2] + (1 - alpha) * event.values[2];
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                //赋值调用clone方法
//            magneticValues = event.values.clone();
                magneticValues[0]=alpha * magneticValues[0] + (1 - alpha) * event.values[0];
                magneticValues[1]=alpha * magneticValues[1] + (1 - alpha) * event.values[1];
                magneticValues[2]=alpha * magneticValues[2] + (1 - alpha) * event.values[2];
            }
            float[] R = new float[9];
            float[] I = new float[9];
            float[] values = new float[3];
            boolean success=SensorManager.getRotationMatrix(R,I,accelerometerValues,magneticValues);
            SensorManager.getOrientation(R, values);
//【2.2】根据inclination matrix计算磁仰角，地球表面任一点的地磁场总强度的矢量方向与水平面的夹角。
            double mInclination = SensorManager.getInclination(I);
//        Log.i(TAG, "onSensorChanged: -----磁场强度："+henceValue);
//        Log.d("Main","values[0] :"+Math.toDegrees(values[0]));
            //values[0]的取值范围是-180到180度。
            //将计算出的旋转角度取反，用于旋转指南针背景图
            //+-180表示正南方向，0度表示正北，-90表示正西，+90表示正东
            int mAzimuth = (int) Math.toDegrees(values[0]);
            int valueNew,valueNew1;
            if (mAzimuth < 0) {
                valueNew = mAzimuth + 360;
            } else {
                valueNew = mAzimuth;
            }
            float SmoothFactorCompass = 0.95f;
            float SmoothThresholdCompass = 10.0f;
            if(Math.abs(compassNumber-valueNew)<180){
                if(Math.abs(compassNumber-valueNew)>SmoothThresholdCompass){
                    valueNew1=valueNew;
                }else {
                    valueNew1 = (int) (compassNumber + SmoothFactorCompass * (valueNew - compassNumber));
                }
            }else{
                if (360.0 - Math.abs(valueNew - compassNumber) > SmoothThresholdCompass) {
                    valueNew1 = valueNew;
                }else {
                    if (compassNumber > valueNew) {
                        valueNew1 = (int) ((compassNumber + SmoothFactorCompass * ((360 + valueNew - compassNumber) % 360) + 360) % 360);
                    } else {
                        valueNew1 = (int) ((compassNumber - SmoothFactorCompass * ((360 - valueNew + compassNumber) % 360) + 360) % 360);
                    }
                }
            }
            if (Math.abs(compassNumber-valueNew1)>=1 && success) {
//        if ( success) {
                compassNumber = valueNew1;
                int pitchNumber= (int) Math.toDegrees(values[1]);//俯仰角
                int rollNumber= (int) Math.toDegrees(values[2]);//横滚角
                int  elevationNumber= (int) Math.toDegrees(mInclination);

                int henceValue= (int)((I[3]*R[0]+I[4]*R[3]+I[5]*R[6])*magneticValues[0]+
                        (I[3]*R[1]+I[4]*R[4]+I[5]*R[7])*magneticValues[1]+
                        (I[3]*R[2]+I[4]*R[5]+I[5]*R[8])*magneticValues[2]);
                Log.i("TAG", "onSensorChanged: magneticValues:"+ Arrays.toString(magneticValues)+"_____accelerometerValues:"+ Arrays.toString(accelerometerValues));
//                mHandler.removeMessages(MESSAGE_UPDATE_COMPASSNUMBER);
//                mHandler.sendEmptyMessage(MESSAGE_UPDATE_COMPASSNUMBER);//更新指南针
            }


            miui.setVal(compassNumber);


//            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//                val = event.values[0];
//                Log.d("TAG", "onSensorChanged: val = "+val);
//                miui.setVal(val);
//            }
//            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
//                accValues=event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
//            }
//            else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
//                magValues=event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
//            }
//            /**public static boolean getRotationMatrix (float[] R, float[] I, float[] gravity, float[] geomagnetic)
//             * 填充旋转数组r
//             * r：要填充的旋转数组
//             * I:将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
//             * gravity:加速度传感器数据
//             * geomagnetic：地磁传感器数据
//             */
//            SensorManager.getRotationMatrix(r, null, accValues, magValues);
//            /**
//             * public static float[] getOrientation (float[] R, float[] values)
//             * R：旋转数组
//             * values ：模拟方向传感器的数据
//             */
//
//            SensorManager.getOrientation(r, values);
//
//
//            //将弧度转化为角度后输出
//            StringBuffer buff=new StringBuffer();
//            for(float value:values){
//                value=(float) Math.toDegrees(value);
//                buff.append(value+"  ");
//            }
//            Log.d("TAG", "onSensorChanged: buff = "+buff.toString());




//            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
//                val = event.values[0];
//                System.out.println(val);
////                miui.setVal(val);
//            } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                mAccelerometerReading = event.values.clone();
//                System.out.println("accelerometer data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
////                Log.d(Tag, "accelerometer data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//                mAccelerometerSensorTextView.setText("accelerometer [x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                mMagneticFieldReading = event.values.clone();
//                System.out.println("magnetic data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
////                Log.d(Tag, "magnetic data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//                mMagneticSensorTextView.setText("magnetic [x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                System.out.println("gyroscope data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
////                Log.d(Tag, "gyroscope data[x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//                mGyroscopeSensorTextView.setText("gyroscope [x:" + event.values[0] + ", y:" + event.values[1] + ", z:" + event.values[2] + "]");
//            }
//            calculateOrientation();
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            System.out.println("onAccuracyChanged:" + sensor.getType() + "->" + accuracy);
            if (accuracy >= SensorManager.SENSOR_STATUS_ACCURACY_HIGH ) {
                System.out.println("Compass, 不需要校验");
//                Log.e("Compass", " 不需要校验");
                mAdjustTextView.setText("Compass, 不需要校验");
            } else {
                System.out.println("Compass, 需要校准 ");
//                Log.e("Compass", " 需要校准 ");
                mAdjustTextView.setText("Compass, 需要校验, 拿手机画8");
            }

        }

    }


}
