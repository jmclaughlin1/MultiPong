package com.example.johnny.commonframeworkmodule;

import android.content.Intent;
import android.content.IntentFilter;

/**
 * This interface lets the components that implement it define how it processes messages sent by
 * other activities/services, and also which message IDs it can receive.
 *
 * @author Johnny McLaughlin
 */

interface MessageListener {

    /**
     * Defines how messages are processed.
     * @param intent the object containing the message
     */
    void processMessage(Intent intent);

    /**
     * Accesses the list of valid message IDs.
     * @return the valid message IDs
     */
    IntentFilter getValidMessages();
}
