package com.example.johnny.commonframeworkmodule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * This classes defines the messaging interface between different components (i.e., activities and
 * services). It defines publishing and receiving messages sent by other activities or services.
 *
 * @author Johnny McLaughlin
 */
class MessageInterface {

    // object that receives messages
    private MessageReceiver receiver;

    // determines which message IDs can be received
    private IntentFilter filter;

    // interface that defines how messages are processed
    private MessageListener listener;

    /**
     * Constructs the message interface object.
     * @param myListener
     */
    public MessageInterface(MessageListener myListener) {
        listener = myListener;
        receiver = new MessageReceiver(listener);
        filter = listener.getValidMessages();
    }

    // Access the message receiver
    public BroadcastReceiver getReceiver() {
        return receiver;
    }

    // Access the valid message ID list
    public IntentFilter getFilter() {
        return filter;
    }

    /**
     * Sends a message over the interface.
     * @param context the component sending the message
     * @param id the message ID
     * @param body the message body
     */
    public void publishMessage(Context context, String id, int[] body) {
        Intent intent = new Intent(id);
        intent.putExtra(Messages.MESSAGE_BODY, body);

        context.sendBroadcast(intent);
    }

    /**
     * This is a private class that implements a message receiver. It processes the message based on
     * what messages the component is allowed to process, and what the component wants to do with
     * the message.
     */
    private class MessageReceiver extends BroadcastReceiver {

        // Defines how the component wants to process messages.
        private MessageListener theListener;

        /**
         * Constructs the message receiver.
         * @param mListener the defining listening interface
         */
        public MessageReceiver(MessageListener mListener) {
            theListener = mListener;
        }

        /**
         * Called when a message has been received.
         * @param context the component receiving the message
         * @param intent the object containing the message
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getAction();
            int[] body = intent.getExtras().getIntArray(Messages.MESSAGE_BODY);
            theListener.processMessage(id, body);
        }
    }
}
