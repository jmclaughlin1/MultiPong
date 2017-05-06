package com.example.johnny.multipong.Bluetooth;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.johnny.multipong.BaseActivity;
import com.example.johnny.multipong.R;

/**
 * Created by Jason Esquivel on 5/6/2017.
 */

public class BluetoothTestActivity extends BaseActivity {
    // Return Intent extra
    public static String ACTION = "bluetooth_action";
    public static String ACTION_WAIT = "wait";
    public static String ACTION_CONNECT = "connect";
    public static String DEVICE_ADDRESS = "device_address"; // only sent when action is to connect
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        String action = getIntent().getExtras().getString(ACTION);
        String address = "";
        if(action.equals(ACTION_CONNECT)){
            address = getIntent().getExtras().getString(DEVICE_ADDRESS);
        }
        else if(action.equals(ACTION_WAIT)){
            address = "";
        }

        Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
        bluetoothServiceIntent.putExtra(ACTION, action);
        bluetoothServiceIntent.putExtra(DEVICE_ADDRESS, address);

        startService(bluetoothServiceIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void processActivityMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidActivityMessages() {
        IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }
}
