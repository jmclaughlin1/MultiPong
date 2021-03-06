package com.example.johnny.multipong;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.johnny.multipong.Bluetooth.BluetoothTestActivity;
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

        ImageButton findFriend = (ImageButton) findViewById(R.id.findf);
        findFriend.setOnClickListener(this);

        ImageButton waitFriend = (ImageButton) findViewById(R.id.waitf);
        waitFriend.setOnClickListener(this);

        ImageButton optionsButton = (ImageButton) findViewById(R.id.optionsbutton);
        optionsButton.setOnClickListener(this);

        startService(new Intent(this, SoundService.class));
        startService(new Intent(this, SensorsService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.optionsbutton:
                Intent intentOptions = new Intent(MenuActivity.this, OptionsActivity.class);
                startActivity(intentOptions);
                break;
            case R.id.findf:
                Intent intentFind = new Intent(MenuActivity.this, DeviceListActivity.class);
                intentFind.putExtra(DataModel.PLAYER_ID, DataModel.PLAYER_1);
                startActivity(intentFind);
                break;
            case R.id.waitf:
                Intent intentWait = new Intent(MenuActivity.this, BluetoothTestActivity.class);
                intentWait.putExtra(BluetoothTestActivity.ACTION, BluetoothTestActivity.ACTION_WAIT);
                intentWait.putExtra(DataModel.PLAYER_ID, DataModel.PLAYER_2);
                startActivity(intentWait);
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
