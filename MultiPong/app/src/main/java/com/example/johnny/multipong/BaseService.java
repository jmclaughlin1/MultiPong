package com.example.johnny.multipong;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * This is a super class that all services will inherit from. It initializes the thread that the
 * service will operate in, and establishes the messaging interface the service will use to
 * communicate with other services and activities.
 *
 * @author Johnny McLaughlin
 */
public abstract class BaseService extends Service {

    // the messaging interface object
    private MessageInterface messageInterface;

    // defines how services will process the message
    private MessageListener messageListener;

    // the thread the service will operate in
    private Thread thread;

    /**
     * Default constructor
     */
    public BaseService() {
    }

    /**
     * Called when the service is created. Start the thread and construct the inter-component
     * messaging interface.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // The service that extends this class will define how it processes it's messages.
                messageListener  = new MessageListener() {

                    /**
                     * Defines how the message is processed
                     * @param id the message ID
                     * @param body the message body
                     */
                    @Override
                    public void processMessage(String id, int[] body) {
                        processServiceMessage(id, body);
                    }

                    /**
                     * Accesses the list of valid message IDs
                     * @return the list of valid message IDs
                     */
                    @Override
                    public IntentFilter getValidMessages() {
                        return getValidServiceMessages();
                    }
                };

                messageInterface = new MessageInterface(messageListener);
                registerReceiver(messageInterface.getReceiver(), messageInterface.getFilter());

                runService();
            }
        });

        thread.start();
    }

    /**
     * Called when the service is destroyed. Closes the message receiver.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(messageInterface.getReceiver());

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    /**
     * Sends a message over the interface.
     * @param id the message ID
     * @param body the message body
     */
    public void publishServiceMessage(String id, int[] body) {
        messageInterface.publishMessage(this, id, body);
    }

    /**
     * Handles when a service has been binded by an activity (not used)
     * @param intent the intent
     * @return an exception
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Processes the message received by the service. Defined by the child service.
     * @param id the message ID
     * @param body the message body
     */
    public abstract void processServiceMessage(String id, int[] body);

    /**
     * Accesses the list of valid message IDs this service can receive.
     * @return the valid message IDs
     */
    public abstract IntentFilter getValidServiceMessages();

    /**
     * Defines what the service will do inside the thread.
     */
    public abstract void runService();
}
