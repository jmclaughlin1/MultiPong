package com.example.johnny.multipong;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;
import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;


public class SensorsService extends BaseService {
    SensorManager sensorManager;
    Sensor sensor_gyro,sensor_acc;
    float time;
    float currentvalX=0,currentvalY=0,currentvalZ=0;
    float sensor_cal=0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    public float gyroX=0,gyroY=1,gyroZ=2;
    private Timer gyroTimer;

    public SensorsService ()

    {
        // sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //sensor_gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // sensor_acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // sensorManager.registerListener(gyroListener, sensor_gyro, 10005000);//SensorManager.SENSOR_DELAY_NORMAL
        //sensorManager.registerListener(accListener, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
        gyroTimer = new Timer();
    }
    @Override
    public void processServiceMessage(String id, int[] body) {


    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFiltersensor= new IntentFilter();

        return intentFiltersensor;
    }

    @Override
    public void runService() {
    Log.i("SensorService","Service Started");
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int i;float gyro_cal;
            float N2NS = 1.0f/1000000000f;
            float Xoffset;
             gyroX = event.values[0];
             gyroY = event.values[1];
             gyroZ = event.values[2];
            Xoffset =0.5f;
            time= event.timestamp;
            timestamp = (float) event.timestamp - time;
           /* for (i=0;i<=1000;i++)
            {
                x += event1.values[0];

            }
           // x /= 1000; //averaging the filter values//

           // gyro_cal = event1.values[0]- x;
            */


            //for (j=0;j<=)
            /*
            textX_G.setText("Rot-X :" + (x*100));
            textY_G.setText("Rot-Y :" + (timestamp));
            textZ_G.setText("Rot-Z :" + (int)z );
            */
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public SensorEventListener accListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor_acc, int acc) { }

        public void onSensorChanged(SensorEvent event2) {
            float a = event2.values[0];
            float b = event2.values[1];
            float c = event2.values[2];




            //textX_A.setText("X : " + a + " m/s2");
            // textY_A.setText("Y : " + b + " m/s2");
            // textZ_A.setText("Z : " + c + " m/s2");

        }
    };

    private class SensorTask extends TimerTask {


        @Override
        public void run() {
            int body[] = new int[3];
            body[Messages.GyroscopeMessage.GYROSCOPE_X] = (int) gyroX;
            body[Messages.GyroscopeMessage.GYROSCOPE_Y] = (int) gyroY;
            body[Messages.GyroscopeMessage.GYROSCOPE_Z] = (int) gyroZ;
            publishServiceMessage(Messages.GyroscopeMessage.GYROSCOPE_MESSAGE_ID,body);
            Toast.makeText(SensorsService.this, "Values Broadcasted", Toast.LENGTH_SHORT).show();
        }
    }
}
