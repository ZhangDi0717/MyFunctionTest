package com.zhangdi.myfunctiontest.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zhangdi.myfunctiontest.MiCompass;
import com.zhangdi.myfunctiontest.R;

public class Compass2Activity extends AppCompatActivity {
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private MiCompass miui;
    private  float val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass2);
        miui = findViewById(R.id.miui);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                val = sensorEvent.values[0];
                System.out.println(val);
                miui.setVal(val);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
//        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
//        ,sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }

}
