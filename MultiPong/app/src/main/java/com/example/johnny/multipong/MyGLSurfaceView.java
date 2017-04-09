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

    public MyGLSurfaceView(Context context){
        super(context);
        Log.i(TAG, "MyGLSurfaceView");
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent (MotionEvent e) {
        Log.i(TAG, "onTouchEvent");
        float x = e.getX();
        float y = e.getY();

        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x < 100 && y > 600)
                    mRenderer.togglePause();
               Log.i(TAG,  "(X,Y) = " + x + ", "+ y);
        }
        return true;
    }
}
