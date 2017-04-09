package com.example.johnny.multipong;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/**
 * This is the Service used for receiving the raw sensor values.
 *
 * Accelerometer_X  Gyroscope_X
 * Accelerometer_Y  Gyroscope_Y
 * Accelerometer_Z  Gyroscope_Z are the raw sensor values.
 *
 * Author: Onkar
 */
public class SensorsService extends BaseActivity {
    SensorManager sensorManager;
    Sensor sensor_gyro,sensor_acc;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void processActivityMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidActivityMessages() {
        return null;
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, sensor_gyro,
                SENSOR_DELAY_NORMAL);//SensorManager.SENSOR_DELAY_NORMAL
        sensorManager.registerListener(accListener, sensor_acc,
                SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor_gyro, int acc) { }

        public void onSensorChanged(SensorEvent event1) {
            float Gyroscope_X = event1.values[0];
            float Gyroscope_Y = event1.values[1];
            float Gyroscope_Z = event1.values[2];
        }
    };
    public SensorEventListener accListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor_acc, int acc) { }

        public void onSensorChanged(SensorEvent event2) {
            float Accelerometer_X = event2.values[0];
            float Accelerometer_Y = event2.values[1];
            float Accelerometer_Z = event2.values[2];
        }
    };

    //@Override
    public void processServiceMessage(String id, int[] body) {

    }

    //@Override
    public IntentFilter getValidServiceMessages() {
        return null;
    }

    //@Override
    public void runService() {

    }
}
