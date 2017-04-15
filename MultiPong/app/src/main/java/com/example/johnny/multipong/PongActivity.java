package com.example.johnny.multipong;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PongActivity extends BaseActivity {
    private GLSurfaceView mGLView;

    private int drawBall;
    private int ballX;
    private int ballY;
    private int paddleX;
    private int paddleY;
    private int paddleAngle;

    private int ballRadius;
    private int paddleWidth;
    private int paddleHeight;
    private boolean validFields = true;

    
    private final String TAG = "PongActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);


        Log.i(TAG, " DataModel Service Started");
        //FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        //setContentView(R.layout.activity_main);

        //TextView mPlayer1 = (TextView) frameLayout.findViewById(R.id.player1Text);
        //TextView mPlayer2 = (TextView) frameLayout.findViewById(R.id.player2Text);

        //ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //ConfigurationInfo info = am.getDeviceConfigurationInfo();
        //boolean supportES2 = (info.reqGlEsVersion >= 0x20000);
        //if (supportES2) {
        //    Log.i(TAG, "Your device OpenGLES2 version (" + info.reqGlEsVersion + ")");
        mGLView = new MyGLSurfaceView(this);
        //    frameLayout.addView(mGLView);
        setContentView(mGLView);
        //} else
        //    Log.e("OpenGL2", "your device doesn't support ES2. (" + info.reqGlEsVersion + ")");

        //mPlayer1.bringToFront();
        //mPlayer2.bringToFront();
        startService(new Intent(this, DataModel.class));
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
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    // if states id parameter with all ids in getValidActivityMessges,
    // body is message body
    public void processActivityMessage(String id, int[] body) {
        Log.i(TAG, "processActivityMessage");
        if (id.equals(Messages.InitMessage.INIT_MESSAGE_ID)){
            Log.i(TAG, Messages.InitMessage.INIT_MESSAGE_ID);
            ballRadius      = body[Messages.InitMessage.BALL_RADIUS];
            paddleWidth     = body[Messages.InitMessage.PADDLE_WIDTH];
            paddleHeight    = body[Messages.InitMessage.PADDLE_HEIGHT];

            validFields = true;
            Log.i(TAG, ballRadius + " " + paddleWidth + " " + paddleHeight);
        }
        else if(id.equals(Messages.PositionMessage.POSITION_MESSAGE_ID)){
            Log.i(TAG, Messages.PositionMessage.POSITION_MESSAGE_ID);
            drawBall    = body[Messages.PositionMessage.DRAW_BALL];
            ballX       = body[Messages.PositionMessage.BALL_X];
            ballY       = body[Messages.PositionMessage.BALL_Y];
            paddleX     = body[Messages.PositionMessage.PADDLE_X];
            paddleY     = body[Messages.PositionMessage.PADDLE_Y];
            paddleAngle = body[Messages.PositionMessage.PADDLE_ANGLE];

            Log.i(TAG, ballX + " " + ballY );
        }
    }
    // create intent filter
    // add message ids gonna receive to intent filter

    @Override
    public IntentFilter getValidActivityMessages() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Messages.InitMessage.INIT_MESSAGE_ID);
        filter.addAction(Messages.PositionMessage.POSITION_MESSAGE_ID);
        return filter;
    }

    /*public boolean getValidAttributes(){
        return validAttributes;
    }*/

    public int getBallRadius() {
        return ballRadius;
    }
    public int getPaddleWidth() {
        return paddleWidth;
    }
    public int getPaddleHeight() {
        return paddleHeight;
    }
    public boolean getValidFields() {
        return validFields;
    }
    public int getBallX(){
        return ballX;
    }
    public int getBallY(){
        return ballY;
    }
    public int getPaddleX(){
        return paddleX;
    }
    public int getPaddleY(){
        return paddleY;
    }
    public int getPaddleAngle(){
        return paddleAngle;
    }
}