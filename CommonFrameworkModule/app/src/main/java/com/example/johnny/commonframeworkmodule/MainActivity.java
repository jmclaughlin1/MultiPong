package com.example.johnny.commonframeworkmodule;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private int field1;

    private int field2;

    private int field3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        field1 = 0;
        field2 = 0;
        field3 = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateFields();

        startService(new Intent(this, TestService.class));
    }

    @Override
    public void processActivityMessage(String id, int[] body) {
        if (id.equals(Messages.TestMessage.TEST_MESSAGE_ID)) {

            field1 = body[Messages.TestMessage.TEST_FIELD_1];
            field2 = body[Messages.TestMessage.TEST_FIELD_2];
            field3 = body[Messages.TestMessage.TEST_FIELD_3];

            updateFields();

            field1++;
            field2++;
            field3++;

            sendTestMessageResponse();
        }
    }

    @Override
    public IntentFilter getValidActivityMessages() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Messages.TestMessage.TEST_MESSAGE_ID);
        return filter;
    }

    public void sendTestMessageResponse() {
        int[] myBody = new int[Messages.TestMessageResponse.TEST_MESSAGE_SIZE];
        myBody[Messages.TestMessageResponse.TEST_FIELD_1] = field1;
        myBody[Messages.TestMessageResponse.TEST_FIELD_2] = field2;
        myBody[Messages.TestMessageResponse.TEST_FIELD_3] = field3;

        publishActivityMessage(Messages.TestMessageResponse.TEST_MESSAGE_ID, myBody);
    }

    private void updateFields() {
        TextView field1View = (TextView) findViewById(R.id.field1);
        field1View.setText(Integer.toString(field1));
        TextView field2View = (TextView) findViewById(R.id.field2);
        field2View.setText(Integer.toString(field2));
        TextView field3View = (TextView) findViewById(R.id.field3);
        field3View.setText(Integer.toString(field3));
    }
}
