package com.project.jasonesquivel.pong;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PongActivity extends Activity {
    private GLSurfaceView mGLView;
    
    private final String TAG = "PongActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(frameLayout);

        TextView mScore = (TextView) frameLayout.findViewById(R.id.score);

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        boolean supportES2 = (info.reqGlEsVersion >= 0x20000);
        if (supportES2) {
            Log.i(TAG, "Your device OpenGLES2 version (" + info.reqGlEsVersion + ")");
            mGLView = new MyGLSurfaceView(this);
            frameLayout.addView(mGLView);
            //setContentView(mGLView);
        } else
            Log.e("OpenGL2", "your device doesn't support ES2. (" + info.reqGlEsVersion + ")");

        mScore.bringToFront();
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
        mGLView.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    //@Override
    //protected void onSaveInstanceState(Bundle outState) {
    //    super.onSaveInstanceState(outState);
    //    outState.putStringArray("state", state);
    //    Log.i(TAG, "onSaveInstanceState");
    //}

}