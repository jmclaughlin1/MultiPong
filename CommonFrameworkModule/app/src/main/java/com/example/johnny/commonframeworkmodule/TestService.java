package com.example.johnny.commonframeworkmodule;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

public class TestService extends BaseService {

    private int field1;

    private int field2;

    private int field3;

    public TestService() {
        field1 = 0;
        field2 = 1;
        field3 = 2;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void processServiceMessage(String id, int[] body) {
        if (id.equals(Messages.TestMessageResponse.TEST_MESSAGE_ID)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            field1 = body[Messages.TestMessageResponse.TEST_FIELD_1];
            field2 = body[Messages.TestMessageResponse.TEST_FIELD_2];
            field3 = body[Messages.TestMessageResponse.TEST_FIELD_3];

            sendTestMessage();
        }
    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Messages.TestMessageResponse.TEST_MESSAGE_ID);
        return filter;
    }

    @Override
    public void runService() {
        sendTestMessage();

        while(true);
    }

    public void sendTestMessage() {
        int[] myBody = new int[Messages.TestMessage.TEST_MESSAGE_SIZE];
        myBody[Messages.TestMessage.TEST_FIELD_1] = field1;
        myBody[Messages.TestMessage.TEST_FIELD_2] = field2;
        myBody[Messages.TestMessage.TEST_FIELD_3] = field3;

        publishServiceMessage(Messages.TestMessage.TEST_MESSAGE_ID, myBody);
    }
}
