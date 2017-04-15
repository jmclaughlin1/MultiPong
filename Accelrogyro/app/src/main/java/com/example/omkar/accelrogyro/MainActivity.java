package com.example.omkar.accelrogyro;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static android.R.attr.delay;
import static android.R.attr.y;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {

    TextView textX_G, textY_G, textZ_G,textX_A, textY_A, textZ_A;
    SensorManager sensorManager;
    Sensor sensor_gyro,sensor_acc;
    float time;
    float currentvalX=0,currentvalY=0,currentvalZ=0;
    float sensor_cal=0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX_G = (TextView) findViewById(R.id.textX_G);
        textY_G = (TextView) findViewById(R.id.textY_G);
        textZ_G = (TextView) findViewById(R.id.textZ_G);

        textX_A = (TextView) findViewById(R.id.textX_A);
        textY_A = (TextView) findViewById(R.id.textY_A);
        textZ_A = (TextView) findViewById(R.id.textZ_A);


    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, sensor_gyro, 10005000);//SensorManager.SENSOR_DELAY_NORMAL
        sensorManager.registerListener(accListener, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
       // sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accListener);
        sensorManager.unregisterListener(gyroListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int i;float gyro_cal;
            float N2NS = 1.0f/1000000000f;
            float Xoffset;
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
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
            textX_G.setText("Rot-X :" + (x*100));
            textY_G.setText("Rot-Y :" + (timestamp));
            textZ_G.setText("Rot-Z :" + (int)z );
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
/*
    public SensorEventListener gyro= new SensorEventListener() {
        public float EPSILON= 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            // This time step's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];

                // Calculate the angular speed of the sample
                float omegaMagnitude = (float) sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                // Integrate around this axis with the angular speed by the time step
                // in order to get a delta rotation from this sample over the time step
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) sin(thetaOverTwo);
                float cosThetaOverTwo = (float) cos(thetaOverTwo);
                deltaRotationVector[0] = sinThetaOverTwo * axisX;
                deltaRotationVector[1] = sinThetaOverTwo * axisY;
                deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // rotationCurrent = rotationCurrent * deltaRotationMatrix;

           // currentvalX= currentvalX * deltaRotationVector[0];
          //  currentvalY= currentvalY * deltaRotationVector[1];
          //  currentvalZ= currentvalZ * deltaRotationVector[2];
            textX_G.setText("Rot-X :" + (int)deltaRotationVector[0]);
            textY_G.setText("Rot-Y :" + currentvalY );
            textZ_G.setText("Rot-Z :" + currentvalZ );
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    */
}
