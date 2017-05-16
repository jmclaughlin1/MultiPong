package com.example.johnny.multipong.shapes;

import android.util.Log;

public class Ball extends Shape {

    private static final int NUMBER_POINTS_OUTSIDE = 16;
    private static final int NUMBER_POINTS_TOTAL = NUMBER_POINTS_OUTSIDE + 2;

    private static final float COLOR[] = { 0.0f, 0.0f, 0.0f, 1.0f };

    private float ballCoords[] = new float[(NUMBER_POINTS_TOTAL ) * 3];

    private static final float INITIAL_COORDINATE_X = 0.0f;

    private static final float INITIAL_COORDINATE_Y = 0.75f;

    private static final float INITIAL_ANGLE = 0.0f;

    //Test
    private boolean tempX = true;
    private boolean tempY = true;

    public Ball(float radius){
        super(COLOR, NUMBER_POINTS_TOTAL, INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y, INITIAL_ANGLE );

        ballCoords[0] = 0;
        ballCoords[1] = 0;
        ballCoords[2] = 0;
        Log.v("Thread",""+ballCoords[0]+","+ballCoords[1]+","+ballCoords[2]);

        int index;
        for(int i =1; i < NUMBER_POINTS_TOTAL ; i++){
            index = i * 3;
            ballCoords[index + 0] = (float) (radius * Math.cos(Math.toRadians((float)(i-1) * 360.0/NUMBER_POINTS_OUTSIDE)));
            ballCoords[index + 1] = (float) (radius * Math.sin(Math.toRadians((float)(i-1) * 360.0/NUMBER_POINTS_OUTSIDE)));
            ballCoords[index + 2] = 0;
            Log.v("Thread","Index"+ index+ " " + ballCoords[index + 0] + " , "+ ballCoords[index + 1] + " , " + ballCoords[index + 2]);
        }

        super.setmCoordinates(ballCoords);
        super.initializeVertexBuffer();


    }

    // TEST functions to move paddle
    public void move(float ballX, float ballY)
    {
        Log.i("Ball",ballX + " " + ballY);
        super.setPosX(ballX);
        super.setPosY(ballY);

    }



}