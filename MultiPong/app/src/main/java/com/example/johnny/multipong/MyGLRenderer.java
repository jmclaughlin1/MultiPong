package com.example.johnny.multipong;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.johnny.multipong.shapes.Ball;
import com.example.johnny.multipong.shapes.Paddle;
import com.example.johnny.multipong.shapes.PauseButton;
import com.example.johnny.multipong.shapes.ResumeButton;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jason Esquivel on 3/4/2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final String TAG = "MyGLRenderer";

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private PongActivity mPongActivity;

    private Paddle mPaddle;
    private Ball mBall;
    private PauseButton mPauseLeft, mPauseRight;
    private ResumeButton mResume;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    // GL Handles
    private int mProgram;

    private boolean pause = false;

    private int mWidth;
    private int mHeight;
    private float mRatio;

    private boolean mInitBallPaddle = false;

    public MyGLRenderer(PongActivity pongActivity){
        Log.i(TAG, "MyGLRenderer");
        mPongActivity = pongActivity;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        Log.i(TAG, "onSurfaceCreated");

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mProgram = createGLProgram(vertexShaderCode, fragmentShaderCode);
        // initialize a paddle
        //mPaddle = new Paddle();
        // initialize a ball
        //mBall = new Ball();

        mPauseLeft = new PauseButton();
        mPauseRight = new PauseButton();
        mPauseRight.setPosX(-1.5f);
        mResume = new ResumeButton();

        // Set the camera position (View matrix) - constant
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        //Log.i(TAG, "onDrawFrame");

        initializeBallPaddle(mPongActivity.getValidFields());

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        if(mInitBallPaddle) {
            // Paddle
            mPaddle.move(projPosX(mPongActivity.getPaddleX()), projPosY(mPongActivity.getPaddleY()), mPongActivity.getPaddleAngle());

            mPaddle.draw(mProgram, mMVPMatrix);

            // Ball
            mBall.move(projPosX(mPongActivity.getBallX()), projPosY(mPongActivity.getBallY()));
            mBall.draw(mProgram, mMVPMatrix);
        }


        //Pause
        if(!pause) {
            mPauseLeft.draw(mProgram, mMVPMatrix);
            mPauseRight.draw(mProgram, mMVPMatrix);
        }
        else{
            mResume.draw(mProgram, mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.i(TAG, "onSurfaceChanged");

        GLES20.glViewport(0, 0, width, height);

        mWidth = width;
        mHeight = height;
        mRatio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -mRatio, mRatio, -1, 1, 3, 7);

        sendResolutionMessageToDM();

        Log.i(TAG, "    Width : " + width + " Height : " + height + " Ratio : " + mRatio);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private int createGLProgram(String vertexShaderCode, String fragmentShaderCode){

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        return mProgram;
    }

    public void togglePause(){
        this.pause = !this.pause;
    }

    public int getResolution(){
        return mHeight;
    }

    private void sendResolutionMessageToDM(){
        int[] messageBody = new int[2];
        messageBody[Messages.ScreenResMessage.SCREEN_WIDTH] = mWidth;
        messageBody[Messages.ScreenResMessage.SCREEN_HEIGHT] = mHeight;
        messageBody[Messages.ScreenResMessage.PLAYER_ID] = mPongActivity.getPlayerId();

        mPongActivity.publishActivityMessage(Messages.ScreenResMessage.SCREEN_RES_MESSAGE_ID, messageBody);
    }

    public void initializeBallPaddle(Boolean validFields){
        if(validFields){
            // initialize a paddle
            mPaddle = new Paddle(pixelToScreenX(mPongActivity.getPaddleWidth())/2.0f, pixelToScreenY(mPongActivity.getPaddleHeight())/2.0f);
            // initialize a ball
            mBall = new Ball(pixelToScreenX(mPongActivity.getBallRadius()));

            mInitBallPaddle = true;
        }
    }

    private float pixelToScreenX(int xPixels){
        float x = 2.0f*mRatio/mWidth;

        return (float) xPixels * x;
    }

    private float pixelToScreenY(int yPixels){
        float y = 2.0f/mHeight;

        return (float) yPixels * y;
    }

    private float projPosX(int pixelPosX){
        return ((pixelPosX - (mWidth/2.0f))/(mWidth/2.0f)) * mRatio;
    }

    private float projPosY(int pixelPosY){
        return (pixelPosY - (mHeight/2.0f))/(mHeight/2.0f);
    }
}