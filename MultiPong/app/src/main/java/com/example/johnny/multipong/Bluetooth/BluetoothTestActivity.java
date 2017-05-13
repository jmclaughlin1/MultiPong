package com.example.johnny.multipong.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnny.multipong.BaseActivity;
import com.example.johnny.multipong.Messages;
import com.example.johnny.multipong.R;

/**
 * Created by Jason Esquivel on 5/6/2017.
 */

public class BluetoothTestActivity extends BaseActivity  implements View.OnClickListener{
    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 2;

    // Return Intent extra
    public static String ACTION = "bluetooth_action";
    public static String ACTION_WAIT = "wait";
    public static String ACTION_CONNECT = "connect";
    public static String DEVICE_ADDRESS = "device_address"; // only sent when action is to connect

    //Variable needed to start service
    private String action;
    private String address;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // temp
    private EditText editText;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        action = getIntent().getExtras().getString(ACTION);
        address = "";
        if(action.equals(ACTION_CONNECT)){
            address = getIntent().getExtras().getString(DEVICE_ADDRESS);
        }
        else if(action.equals(ACTION_WAIT)){
            address = "";
        }

        editText = (EditText) findViewById(R.id.testMessage);

        status = (TextView) findViewById(R.id.status);

        Button sendButton = (Button) findViewById(R.id.sendTestMessage);
        sendButton.setOnClickListener(this);

    }
    @Override
    public void onStart(){
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        else {
            Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
            bluetoothServiceIntent.putExtra(ACTION, action);
            bluetoothServiceIntent.putExtra(DEVICE_ADDRESS, address);

            startService(bluetoothServiceIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    Toast.makeText(this, "Bluetooth Enable", Toast.LENGTH_SHORT).show();
                    Intent bluetoothServiceIntent = new Intent(this, BluetoothService.class);
                    bluetoothServiceIntent.putExtra(ACTION, action);
                    bluetoothServiceIntent.putExtra(DEVICE_ADDRESS, address);

                    startService(bluetoothServiceIntent);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendTestMessage:
                int body[] = new int[Messages.BlueToothTestSendMessage.BLUETOOTH_TEST_SEND_MESSAGE_SIZE];
                body[Messages.BlueToothTestSendMessage.TEST] = Integer.parseInt(editText.getText().toString());
                publishActivityMessage(Messages.BlueToothTestSendMessage.BLUETOOTH_TEST_SEND_MESSAGE_ID, body);
                break;
        }
    }
    @Override
    public void processActivityMessage(String id, int[] body) {
        if(id.equals(Messages.BlueToothTestReceiveMessage.BLUETOOTH_TEST_RECEIVE_MESSAGE_ID)){
            status.setText(Integer.toString(body[Messages.BlueToothTestReceiveMessage.TEST]));
        }

    }

    @Override
    public IntentFilter getValidActivityMessages() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Messages.BlueToothTestReceiveMessage.BLUETOOTH_TEST_RECEIVE_MESSAGE_ID);
        return intentFilter;
    }
}
