package com.example.johnny.multipong;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnny.multipong.R;
import com.example.johnny.multipong.sensorFilters.Filters.GyroscopeOrientation;
import com.example.johnny.multipong.sensorFilters.Filters.ImuOCfOrientation;
import com.example.johnny.multipong.sensorFilters.Filters.ImuOCfQuaternion;
import com.example.johnny.multipong.sensorFilters.Filters.ImuOCfRotationMatrix;
import com.example.johnny.multipong.sensorFilters.Filters.ImuOKfQuaternion;
import com.example.johnny.multipong.sensorFilters.Filters.Orientation;
import com.example.johnny.multipong.sensorFilters.ConfigActivity;


public class SensorsService extends BaseService {
    private boolean dataReady = false;

    private boolean imuOCfOrienationEnabled;
    private boolean imuOCfRotationMatrixEnabled;
    private boolean imuOCfQuaternionEnabled;
    private boolean imuOKfQuaternionEnabled;
    private boolean isCalibrated;
    private boolean gyroscopeAvailable;

    private float[] vOrientation = new float[3];
    // Handler for the UI plots so everything plots smoothly
    protected Handler handler;

    private Orientation orientation;

    protected Runnable runable;
    private Thread thread;

    private Timer gyroTimer;

    public SensorsService ()

    {
        gyroTimer = new Timer();
    }

    @Override
    public void processServiceMessage(String id, int[] body) {
        if (id.equals(Messages.RequestCenterPositionMessage.REQUEST_CENTER_POSITION_MESSAGE_ID)) {
            sendCenterPositionMessage();
        }
    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFiltersensor= new IntentFilter();
        intentFiltersensor.addAction(Messages.RequestCenterPositionMessage
                                             .REQUEST_CENTER_POSITION_MESSAGE_ID);

        return intentFiltersensor;
    }

    @Override
    public void runService() {
        Log.i("SensorService","Service Started");

        /*Intent intent = new Intent();
        intent.setClass(this, ConfigActivity.class);
        startActivity(intent);*/

        readPrefs();
        reset();     // put in a constructor

        orientation.reset();

        //handler.post(runable);

        gyroscopeAvailable = gyroscopeAvailable();

        gyroTimer.scheduleAtFixedRate(new SensorTask() ,0,33);
    }

    private class SensorTask extends TimerTask {

        @Override
        public void run() {
            vOrientation = orientation.getOrientation();
            //Log.i("SensorService", Math.toDegrees(vOrientation[0]) + " " + Math.toDegrees(vOrientation[1]) + " " + Math.toDegrees(vOrientation[2]));
            dataReady = true;
            sendGyroMessage();
        }
    }

    private void sendGyroMessage() {
        int body[] = new int[Messages.GyroscopeMessage.GYROSCOPE_MESSAGE_SIZE];
        body[Messages.GyroscopeMessage.GYROSCOPE_X] = (int) Math.toDegrees(vOrientation[1]);
        body[Messages.GyroscopeMessage.GYROSCOPE_Y] = (int) Math.toDegrees(vOrientation[2]);
        body[Messages.GyroscopeMessage.GYROSCOPE_Z] = (int) Math.toDegrees(vOrientation[0]);

        //Log.i("Sensor", "Z: " + body[Messages.GyroscopeMessage.GYROSCOPE_Z]);
        publishServiceMessage(Messages.GyroscopeMessage.GYROSCOPE_MESSAGE_ID, body);
    }

    private void sendCenterPositionMessage() {
        int body[] = new int[Messages.CenterPositionMessage.CENTER_POSITION_MESSAGE_SIZE];
        body[Messages.CenterPositionMessage.LEFT_Y_FIELD] = -50;
        body[Messages.CenterPositionMessage.RIGHT_Y_FIELD] = 50;
        body[Messages.CenterPositionMessage.LEFT_X_FIELD] = -50;
        body[Messages.CenterPositionMessage.RIGHT_X_FIELD] = 50;
        body[Messages.CenterPositionMessage.CENTER_Y_FIELD] = 0;
        body[Messages.CenterPositionMessage.CENTER_X_FIELD] = 25;

        publishServiceMessage(Messages.CenterPositionMessage.CENTER_POSITION_MESSAGE_ID, body);
    }

    private boolean getPrefCalibratedGyroscopeEnabled()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return prefs.getBoolean(
                ConfigActivity.CALIBRATED_GYROSCOPE_ENABLED_KEY, true);
    }

    private boolean getPrefImuOCfOrientationEnabled()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return prefs.getBoolean(ConfigActivity.IMUOCF_ORIENTATION_ENABLED_KEY,
                false);
    }

    private boolean getPrefImuOCfRotationMatrixEnabled()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return prefs.getBoolean(
                ConfigActivity.IMUOCF_ROTATION_MATRIX_ENABLED_KEY, false);
    }

    private boolean getPrefImuOCfQuaternionEnabled()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return prefs.getBoolean(ConfigActivity.IMUOCF_QUATERNION_ENABLED_KEY,
                false);
    }

    private boolean getPrefImuOKfQuaternionEnabled()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return prefs.getBoolean(ConfigActivity.IMUOKF_QUATERNION_ENABLED_KEY,
                false);
    }

    private float getPrefImuOCfOrienationCoeff()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return Float.valueOf(prefs.getString(
                ConfigActivity.IMUOCF_ORIENTATION_COEFF_KEY, "0.5"));
    }

    private float getPrefImuOCfRotationMatrixCoeff()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return Float.valueOf(prefs.getString(
                ConfigActivity.IMUOCF_ROTATION_MATRIX_COEFF_KEY, "0.5"));
    }

    private float getPrefImuOCfQuaternionCoeff()
    {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        return Float.valueOf(prefs.getString(
                ConfigActivity.IMUOCF_QUATERNION_COEFF_KEY, "0.5"));
    }

    private boolean gyroscopeAvailable()
    {
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    /**
     * Log output data to an external .csv file.
     */
    private void reset()
    {
        isCalibrated = getPrefCalibratedGyroscopeEnabled();

        orientation = new GyroscopeOrientation(this);

        /*if (isCalibrated)
        {
            //tvStatus.setText("Sensor Calibrated");
        }
        else
        {
            //tvStatus.setText("Sensor Uncalibrated");
        }

        if (imuOCfOrienationEnabled)
        {
            Log.i("SensorService", "We have an IMU OCF Orientation Enabled!");
            orientation = new ImuOCfOrientation(this);
            orientation.setFilterCoefficient(getPrefImuOCfOrienationCoeff());

            if (isCalibrated)
            {
                //tvStatus.setText("ImuOCfOrientation Calibrated");
            }
            else
            {
                //tvStatus.setText("ImuOCfOrientation Uncalibrated");
            }

        }
        else if (imuOCfRotationMatrixEnabled)
        {
            Log.i("SensorService", "We have an IMU OCF Rotation Matrix Enabled!");
            orientation = new ImuOCfRotationMatrix(this);
            orientation
                    .setFilterCoefficient(getPrefImuOCfRotationMatrixCoeff());

            if (isCalibrated)
            {
                //tvStatus.setText("ImuOCfRm Calibrated");
            }
            else
            {
               // tvStatus.setText("ImuOCfRm Uncalibrated");
            }
        }
        else if (imuOCfQuaternionEnabled)
        {
            Log.i("SensorService", "We have an IMU OCF Quaternion Enabled!");
            orientation = new ImuOCfQuaternion(this);
            orientation.setFilterCoefficient(getPrefImuOCfQuaternionCoeff());

            if (isCalibrated)
            {
                //tvStatus.setText("ImuOCfQuaternion Calibrated");
            }
            else
            {
                //tvStatus.setText("ImuOCfQuaternion Uncalibrated");
            }
        }*/
        //else if (imuOKfQuaternionEnabled)
        //{
            //Log.i("SensorService", "We have an IMU OK Quaternion Enabled!");
            //orientation = new ImuOKfQuaternion(this);

            if (isCalibrated)
            {
               // tvStatus.setText("ImuOKfQuaternion Calibrated");
            }
            else
            {
               // tvStatus.setText("ImuOKfQuaternion Uncalibrated");
            }
        /*} else {
            Log.i("SensorService", "We have NOTHING!");
        }*/

        if (gyroscopeAvailable)
        {
           // tvStatus.setTextColor(this.getResources().getColor(
                    //R.color.light_green));
        }
        else
        {
            //tvStatus.setTextColor(this.getResources().getColor(
         //   R.color.light_red));

            //showGyroscopeNotAvailableAlert();
        }
        /*Looper.prepare();
        handler = new Handler();

        runable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 100);

                vOrientation = orientation.getOrientation();
                Log.i("SensorService", Math.toDegrees(vOrientation[0]) + " " + Math.toDegrees(vOrientation[0]) + " " + Math.toDegrees(vOrientation[0]));
                dataReady = true;

                //updateText();

            }
        };*/
    }

    private void readPrefs()
    {
        imuOCfOrienationEnabled = getPrefImuOCfOrientationEnabled();
        imuOCfRotationMatrixEnabled = getPrefImuOCfRotationMatrixEnabled();
        imuOCfQuaternionEnabled = getPrefImuOCfQuaternionEnabled();
        imuOKfQuaternionEnabled = getPrefImuOKfQuaternionEnabled();
    }

    private void showGyroscopeNotAvailableAlert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Gyroscope Not Available");

        // set dialog message
        alertDialogBuilder
                .setMessage(
                        "Your device is not equipped with a gyroscope or it is not responding. This is *NOT* a problem with the app, it is problem with your device.")
                .setCancelable(false)
                .setNegativeButton("I'll look around...",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Begin logging data to an external .csv file.
     */
    private void updateText()
    {
        Toast.makeText(getApplicationContext(), String.format("%.2f", Math.toDegrees(vOrientation[1])), Toast.LENGTH_SHORT).show();
    }
}
