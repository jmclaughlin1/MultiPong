package com.example.johnny.multipong;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Jason Esquivel on 3/4/2017.
 */

class MyGLSurfaceView extends GLSurfaceView {
    private final String TAG = "MyGLSurfaceView";

    private final MyGLRenderer mRenderer;

    public static int resolution720 = 0;
    public static int resolution1080 = 1;
    public static int resolution1440 = 2;

    private PongActivity mPongActivity;

    public MyGLSurfaceView(PongActivity pongActivity, boolean player){
        super(pongActivity);
        Log.i(TAG, "MyGLSurfaceView");
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mPongActivity = pongActivity;

        mRenderer = new MyGLRenderer(pongActivity, player);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        Log.i(TAG, "onTouchEvent");
        int x = (int)e.getX();
        int y = (int)e.getY();

        int xPauseLimit = 100;
        int yPauseLimit = 600;

        if(mRenderer.getResolution() == 1440){
            xPauseLimit = 200;
            yPauseLimit = 1200;
        }
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x < xPauseLimit && y > yPauseLimit) {
                    mRenderer.togglePause();
                    int[] messageBody = new int[Messages.PauseMessage.PAUSE_MESSAGE_SIZE];
                    messageBody[Messages.PauseMessage.PAUSE_RESUME_FLAG] =  mRenderer.getPause() ? 1 : 0;

                    mPongActivity.publishActivityMessage(Messages.PauseMessage.PAUSE_MESSAGE_ID, messageBody);
                }
               Log.i(TAG,  "(X,Y) = " + x + ", "+ y);
        }
        return true;
    }
}
