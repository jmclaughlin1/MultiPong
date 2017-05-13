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
import com.example.johnny.multipong.Messages;
import com.example.johnny.multipong.R;

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

    //private MessageAdapter mAdapter;

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
                    Log.i(TAG, "Sending message: " + msg);
                    //mAdapter.notifyDataSetChanged();
                    //messageList.add(new BluetoothMessage(counter++, writeMessage, "Me"));
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i(TAG, readMessage);
                    String messageArray[] = readMessage.split("\\|");
                    String id = messageArray[0];
                    if (id.equals(Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_ID)) {
                        Log.i(TAG, "Got Ball transfer message!");
                        int body[] = new int[Messages.BallTransferBTMessage.BALL_TRANSFER_BT_MESSAGE_SIZE];
                        body[Messages.BallTransferBTMessage.BALL_Y] = Integer.parseInt(messageArray[1]);
                        body[Messages.BallTransferBTMessage.BALL_X] = Integer.parseInt(messageArray[2]);
                        body[Messages.BallTransferBTMessage.BALL_ANGLE] = Integer.parseInt(messageArray[3]);

                        publishServiceMessage(Messages.BallTransferBTMessage.BALL_TRANSFER_MESSAGE_BT_ID, body);
                    } else if(id.equals(Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_ID)) {
                        int body [] = new int[Messages.UpdateScoreBTMessage.UPDATE_SCORE_MESSAGE_SIZE];
                        body[Messages.UpdateScoreBTMessage.PLAYER_1_SCORE] = Integer.parseInt(messageArray[1]);
                        body[Messages.UpdateScoreBTMessage.PLAYER_2_SCORE] = Integer.parseInt(messageArray[2]);

                        publishServiceMessage(Messages.UpdateScoreBTMessage.UPDATE_SCORE_MESSAGE_ID, body);
                    } else if(id.equals(Messages.PauseMessage.PAUSE_MESSAGE_ID)) {
                        int body [] = new int[Messages.PauseMessageBT.PAUSE_MESSAGE_BT_SIZE];
                        body[Messages.PauseMessageBT.PAUSE_RESUME_FLAG] = Integer.parseInt(messageArray[1]);
                        publishServiceMessage(Messages.PauseMessageBT.PAUSE_MESSAGE_BT_ID, body);
                    }

                    //Toast.makeText(BluetoothService.this, readMessage, Toast.LENGTH_SHORT).show(); // read your message here
                    //int body[] = new int[Messages.BlueToothTestReceiveMessage.BLUETOOTH_TEST_RECEIVE_MESSAGE_SIZE];
                    //body[Messages.BlueToothTestReceiveMessage.TEST] = Integer.parseInt(readMessage);
                    //publishServiceMessage(Messages.BlueToothTestReceiveMessage.BLUETOOTH_TEST_RECEIVE_MESSAGE_ID, body);
                    //mAdapter.notifyDataSetChanged();
                    //messageList.add(new BluetoothMessage(counter++, readMessage, mConnectedDeviceName));
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    publishServiceMessage(Messages.ConnectedBluetoothMessage.CONNECTED_BLUETOOTH_MESSAGE_ID, null);
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

        //mAdapter = new MessageAdapter(getBaseContext(), messageList);

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

    private void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    @Override
    public void runService() {
    }

    @Override
    public void processServiceMessage(String id, int[] body) {
        String bluetoothMessage = "";

        if(id.equals(Messages.BlueToothTestSendMessage.BLUETOOTH_TEST_SEND_MESSAGE_ID)){
            bluetoothMessage = Integer.toString(body[Messages.BlueToothTestSendMessage.TEST]);
            //sendMessage(Integer.toString(body[Messages.BlueToothTestSendMessage.TEST]));
        } else if (id.equals(Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_ID)) {
            Log.i(TAG, "Sending transfer message");
            bluetoothMessage = Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_ID
                            + "|" + Integer.toString(body[Messages.BallTransferMessage.BALL_Y])
                            + "|" + Integer.toString(body[Messages.BallTransferMessage.BALL_X])
                            + "|" + Integer.toString(body[Messages.BallTransferMessage.BALL_ANGLE]);
        } else if(id.equals(Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_ID)) {
            bluetoothMessage = Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_ID
                            + "|" + Integer.toString(body[Messages.UpdateScoreMessage.PLAYER_1_SCORE])
                            + "|" + Integer.toString(body[Messages.UpdateScoreMessage.PLAYER_2_SCORE]);
        } else if(id.equals(Messages.PauseMessage.PAUSE_MESSAGE_ID)) {
            bluetoothMessage = Messages.PauseMessage.PAUSE_MESSAGE_ID
                    + "|" + Integer.toString(body[Messages.PauseMessage.PAUSE_RESUME_FLAG]);
        }

        sendMessage(bluetoothMessage);
    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Messages.BlueToothTestSendMessage.BLUETOOTH_TEST_SEND_MESSAGE_ID);
        intentFilter.addAction(Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_ID);
        intentFilter.addAction(Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_ID);
        intentFilter.addAction(Messages.PauseMessage.PAUSE_MESSAGE_ID);
        return intentFilter;
    }


}
