package com.example.johnny.commonframeworkmodule;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * This is a super class that all activities will inherit from. It sets up the messaging interface
 * that all activities use to communicate between themselves and services.
 *
 * @author Johnny McLaughlin
 */
public abstract class BaseActivity extends AppCompatActivity {

    // the inter-module messaging interface
    private MessageInterface messageInterface;

    // message listening interface
    private MessageListener messageListener;

    /**
     * Called when the activity starts. Constructs the inter-component messaging interface
     * @param savedInstanceState saved activity data (not used)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageListener  = new MessageListener() {
            @Override
            public void processMessage(Intent intent) {
                processActivityMessage(intent);
            }

            @Override
            public IntentFilter getValidMessages() {
                return getValidActivityMessages();
            }
        };

        messageInterface = new MessageInterface(messageListener);
        registerReceiver(messageInterface.getReceiver(), messageInterface.getFilter());
    }

    /**
     * Called when the activity is destroyed. Closes the receiver of the messaging interface.
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(messageInterface.getReceiver());

        super.onDestroy();
    }

    /**
     * Publishes a message to all services and activities.
     * @param id the message ID
     * @param body the message body
     */
    public void publishActivityMessage(String id, int[] body) {
        messageInterface.publishMessage(this, id, body);
    }

    /**
     * Processes an incoming message.
     * @param intent object containing the message
     */
    public abstract void processActivityMessage(Intent intent);

    /**
     * Accesses the list of valid messages the activity can process.
     * @return object containing the valid messages types
     */
    public abstract IntentFilter getValidActivityMessages();
}
