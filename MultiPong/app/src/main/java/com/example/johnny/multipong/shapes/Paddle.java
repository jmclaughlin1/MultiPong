package com.example.johnny.multipong.shapes;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Paddle extends Shape{

    private static final float COLOR[] = { 0.0f, 0.0f, 0.0f, 1.0f };

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
    public Paddle(float width, float height) {
        super(COLOR, NUMBER_POINTS, INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y, INITIAL_ANGLE );

         float paddleCoords [] = {
                    0.0f, 0.0f, 0.0f,     // center
                    -1.0f*width,  height, 0.0f,   // top right
                    -1.0f*width, -1.0f*height, 0.0f,   // bottom right
                    width, -1.0f*height, 0.0f,   // bottom left
                    width,  height, 0.0f,    // top left
                    -1.0f*width,  height, 0.0f}; // top right

        super.setmCoordinates(paddleCoords);
        super.initializeVertexBuffer();
    }

    // TEST functions to move paddle
    public void move(float paddleX, float paddleY, int paddleAngle)
    {
        super.setPosX(paddleX);
        super.setPosY(paddleY);
        super.setAngle((float) paddleAngle);
    }

}