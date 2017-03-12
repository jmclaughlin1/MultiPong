package com.example.johnny.commonframeworkmodule;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class TestService extends BaseService {
    public TestService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void processServiceMessage(Intent intent) {

    }

    @Override
    public IntentFilter getValidServiceMessages() {
        return null;
    }

    @Override
    public void runService() {
        sendTestMessage();
    }

    public void sendTestMessage() {
        int[] myBody = new int[3];
        myBody[0] = 3;
        myBody[1] = 54;
        myBody[2] = 17;

        publishServiceMessage(Messages.TEST_MESSAGE, myBody);
    }
}
