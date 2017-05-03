package com.example.johnny.multipong;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
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
        gyroTimer.scheduleAtFixedRate(new SensorTask() ,100,500);
        initUI();
        readPrefs();
        reset();     // put in a constructor

        orientation.reset();

        handler.post(runable);



        gyroscopeAvailable = gyroscopeAvailable();




    }
    @Override
    public void processServiceMessage(String id, int[] body) {


    }

    @Override
    public void onCreate() {
        super.onCreate();

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





    private class SensorTask extends TimerTask {


        @Override
        public void run() {
           // int body[] = new int [1];
         //   body[Messages.GyroscopeMessage.GYROSCOPE_X] = (int) gyroX;
           // body[Messages.GyroscopeMessage.GYROSCOPE_Y] = (Math.toDegrees(vOrientation[1]));
          //  body[Messages.GyroscopeMessage.GYROSCOPE_Z] = (int) gyroZ;
           // publishServiceMessage(Messages.GyroscopeMessage.GYROSCOPE_MESSAGE_ID,body);
            Toast.makeText(SensorsService.this, "Values Broadcasted", Toast.LENGTH_SHORT).show();
        }
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
     * Initialize the UI.
     */
    private void initUI()
    {
        // Initialize the calibrated text views
       // tvXAxis = (TextView) this.findViewById(R.id.value_x_axis_calibrated);
       // tvYAxis = (TextView) this.findViewById(R.id.value_y_axis_calibrated);
        //tvZAxis = (TextView) this.findViewById(R.id.value_z_axis_calibrated);
       // tvStatus = (TextView) this.findViewById(R.id.label_sensor_status);


        // Initialize the calibrated gauges views



    }

    /**
     * Log output data to an external .csv file.
     */


    private void reset()
    {
        isCalibrated = getPrefCalibratedGyroscopeEnabled();

        orientation = new GyroscopeOrientation(this);

        if (isCalibrated)
        {
            //tvStatus.setText("Sensor Calibrated");
        }
        else
        {
            //tvStatus.setText("Sensor Uncalibrated");
        }

        if (imuOCfOrienationEnabled)
        {
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
        if (imuOCfRotationMatrixEnabled)
        {
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
        if (imuOCfQuaternionEnabled)
        {
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
        }
        if (imuOKfQuaternionEnabled)
        {
            orientation = new ImuOKfQuaternion(this);

            if (isCalibrated)
            {
               // tvStatus.setText("ImuOKfQuaternion Calibrated");
            }
            else
            {
               // tvStatus.setText("ImuOKfQuaternion Uncalibrated");
            }
        }

        if (gyroscopeAvailable)
        {
           // tvStatus.setTextColor(this.getResources().getColor(
                    //R.color.light_green));
        }
        else
        {
            //tvStatus.setTextColor(this.getResources().getColor(
         //   R.color.light_red));

            showGyroscopeNotAvailableAlert();
        }

        handler = new Handler();

        runable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 100);

                vOrientation = orientation.getOrientation();

                dataReady = true;

                updateText();

            }
        };
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
