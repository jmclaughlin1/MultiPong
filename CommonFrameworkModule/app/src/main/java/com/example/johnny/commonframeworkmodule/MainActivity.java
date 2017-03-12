package com.example.johnny.commonframeworkmodule;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void processActivityMessage(Intent intent) {

    }

    @Override
    public IntentFilter getValidActivityMessages() {
        return null;
    }
}
