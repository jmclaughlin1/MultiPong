package com.project.jasonesquivel.pong.shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jason Esquivel on 3/16/2017.
 */

public class Shape {
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3; // number of coordinates per vertex in this array

    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private float[] mColor;
    private int mNumberPoints;
    private FloatBuffer mVertexBuffer;
    private float mCoordinates[];

    private float posX, posY;
    private float angle;

    public Shape(float[] mColor, int mNumberPoints, float posX, float posY, float angle){
        this.mColor = mColor;
        this.mNumberPoints = mNumberPoints;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
    }
    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(int mProgram, float[] mvpMatrix) {
        float [] finalMatrix = translateMatrixCalc(mvpMatrix, posX, posY);
        // translation before rotation
        finalMatrix = rotationMatrixCalc(finalMatrix, angle);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, mColor, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, finalMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mNumberPoints );

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void setmCoordinates(float[] mCoordinates)
    {
        this.mCoordinates = mCoordinates;
    }

    public void initializeVertexBuffer(){
        // initialize byte buffer for the draw list
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(mCoordinates.length * 4); // (# of coordinate values * 4 bytes per float)
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(mCoordinates);
        mVertexBuffer.position(0);
    }

    /**
     *
     * @param angle Rotation Angle in degrees
     */
    private float[] rotationMatrixCalc(float[] mvpMatrix, float angle){
        float[] mTempMatrix = mvpMatrix.clone();
        float[] mRotationMatrix = new float[16];
        float[] finalMatrix = new float[16];

        // Create a rotation transformation for the triangle
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(finalMatrix, 0, mTempMatrix, 0, mRotationMatrix, 0);

        return finalMatrix;
    }

    private float[] translateMatrixCalc(float[] mvpMatrix, float x, float y){
        float[] finalMatrix = new float[16];

        Matrix.translateM(finalMatrix, 0, mvpMatrix, 0, x * -1.0f, y, 0);
        return finalMatrix;
    }

    public float getPosX()
    {
        return posX;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosX(float posX){
        this.posX = posX;
    }
    public void setPosY(float posY){
        this.posY = posY;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    public float getAngle(){
        return this.angle;
    }
}

