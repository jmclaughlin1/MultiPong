package com.example.johnny.multipong;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.johnny.multipong.Bluetooth.BluetoothChat;
import com.example.johnny.multipong.Bluetooth.DeviceListActivity;

/**
 * Created by Jason Esquivel on 3/25/2017.
 */

public class MenuActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "MenuActivity";

    protected void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = (Button) findViewById(R.id.playbutton);
        playButton.setOnClickListener(this);

        Button optionsButton = (Button) findViewById(R.id.optionsbutton);
        optionsButton.setOnClickListener(this);

        Button testButton = (Button) findViewById(R.id.GameTest);
        testButton.setOnClickListener(this);

        startService(new Intent(this, SoundService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.optionsbutton:
                Intent intentOptions = new Intent(MenuActivity.this, OptionsActivity.class);
                //startActivityForResult(intentOptions, 1);
                startActivity(intentOptions);
                break;
            case R.id.playbutton:
                Intent intentPlay = new Intent(MenuActivity.this, DeviceListActivity.class);
                startActivity(intentPlay);
                break;
            case R.id.GameTest:
                Intent intentTest = new Intent(MenuActivity.this, PongActivity.class);
                startActivity(intentTest);
                break;


        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        resumeBackgroundMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        pauseBackgroundMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopBackgroundMusic();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        stopBackgroundMusic();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }
    @Override
    public void processActivityMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidActivityMessages() {
        IntentFilter filter = new IntentFilter();
        return filter;
    }


}
