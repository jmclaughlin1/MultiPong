package com.example.johnny.multipong.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.johnny.multipong.BaseService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason Esquivel on 4/29/2017.
 */

public class BluetoothService extends BaseService {
    public static final String TAG = "BluetoothService";
    // BluetoothMessage types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Address of the connected Device
    private String connectedDeviceAddress;

    // Name of the connected device
    private String mConnectedDeviceName = null;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    private MessageAdapter mAdapter;

    private List<BluetoothMessage> messageList = new ArrayList<>();

    public int counter = 0;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mAdapter.notifyDataSetChanged();
                    messageList.add(new BluetoothMessage(counter++, writeMessage, "Me"));
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(BluetoothService.this, readMessage, Toast.LENGTH_SHORT).show(); // read your message here
                    mAdapter.notifyDataSetChanged();
                    messageList.add(new BluetoothMessage(counter++, readMessage, mConnectedDeviceName));
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand");
        mAdapter = new MessageAdapter(getBaseContext(), messageList);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);
        Log.i(TAG, "Trying to get extras");
        String action = intent.getExtras().getString(BluetoothTestActivity.ACTION);
        Log.i(TAG, action);
        if(action.equals(BluetoothTestActivity.ACTION_CONNECT)) {

            //connectedDeviceAddress = intent.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            connectedDeviceAddress = intent.getExtras().getString(BluetoothTestActivity.DEVICE_ADDRESS);

            // Get the BLuetoothDevice object
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(connectedDeviceAddress);
            // Attempt to connect to the device
            mChatService.connect(device);
            Toast.makeText(getApplicationContext(), "Connecting to " + connectedDeviceAddress, Toast.LENGTH_SHORT).show();
        }
        if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
            mChatService.start();
        }

        return START_NOT_STICKY;
    }
    @Override
    public void runService() {
    }
    @Override
    public void processServiceMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }


}
