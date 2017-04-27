package com.example.johnny.multipong;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

/**
 * Created by Jason Esquivel on 4/1/2017.
 */

public class OptionsActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "OptionsActivity";
    private static int musicSeekbarValue = 50;
    private static int sfxSeekbarValue = 50;
    protected void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            musicSeekbarValue = savedInstanceState.getInt("musicSeekbarValue");

        }
        Log.i(TAG, "Music Saved Value : " + musicSeekbarValue);
        Log.i(TAG, "SFX   Saved Value : " + musicSeekbarValue);

        setContentView(R.layout.activity_options);

        ImageButton backButton = (ImageButton) findViewById(R.id.backbutton);
        backButton.setOnClickListener(this);

        SeekBar musicSeekBar = (SeekBar)findViewById(R.id.seekBarMusic);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.i(TAG, "Music SeekBar Progress : " + progress);
                musicSeekbarValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        musicSeekBar.setProgress(musicSeekbarValue);

        SeekBar sfxSeekBar = (SeekBar)findViewById(R.id.seekBarSFX);
        sfxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.i(TAG, "SFX   SeekBar Progress : " + progress);
                sfxSeekbarValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sfxSeekBar.setProgress(sfxSeekbarValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton:
                Intent returnIntent = new Intent(OptionsActivity.this, MenuActivity.class);
                //setResult(Activity.RESULT_OK, returnIntent);
                //finish();
                startActivity(returnIntent);
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
        //Log.i(TAG, "    Saved Value : " + musicSeekbarValue);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("musicSeekbarValue", musicSeekbarValue);
        Log.i(TAG, "onSaveInstanceState");
        Log.i(TAG, "musicSeekbarValue : " + musicSeekbarValue);
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

