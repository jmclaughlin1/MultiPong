package com.project.jasonesquivel.pong.shapes;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Paddle extends Shape{


    private static final float paddleCoords[] = {
            0.0f, 0.0f, 0.0f,     // center
            -0.5f,  0.1f, 0.0f,   // top right
            -0.5f, -0.1f, 0.0f,   // bottom right
            0.5f, -0.1f, 0.0f,   // bottom left
            0.5f,  0.1f, 0.0f,    // top left
            -0.5f,  0.1f, 0.0f}; // top right


    private static final float COLOR[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    private static final int NUMBER_POINTS = 6;

    private static final float INITIAL_COORDINATE_X = 0.0f;

    private static final float INITIAL_COORDINATE_Y = -0.75f;

    private static final float INITIAL_ANGLE = 0.0f;


    //Test
    private boolean tempX = true;
    private boolean tempAngle = true;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Paddle() {
        super(COLOR, NUMBER_POINTS, INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y, INITIAL_ANGLE );
        super.setmCoordinates(paddleCoords);
        super.initializeVertexBuffer();
    }

    // TEST functions to move paddle
    public void move(boolean pause)
    {
        if(!pause) {
            float x, angle;
            if (tempX) {
                x = super.getPosX() + 0.01f;
                super.setPosX(x);
                if (x > 1.0f)
                    tempX = false;
            } else {
                x = super.getPosX() - 0.01f;
                super.setPosX(x);
                if (x < -1.0f)
                    tempX = true;
            }

            if (tempAngle) {
                angle = super.getAngle() + 0.5f;
                super.setAngle(angle);
                if (angle > 30.0f)
                    tempAngle = false;
            } else {
                angle = super.getAngle() - 0.5f;
                super.setAngle(angle);
                if (angle < -30.0f)
                    tempAngle = true;
            }
        }
    }

}