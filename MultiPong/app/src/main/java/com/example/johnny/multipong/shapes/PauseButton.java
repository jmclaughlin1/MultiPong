package com.example.johnny.multipong.shapes;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class PauseButton extends Shape{


    private static final float pauseCoords[] = {
            0.0f, 0.0f, 0.0f,     // center
            -0.03f,  0.08f, 0.0f,   // top right
            -0.03f, -0.08f, 0.0f,   // bottom right
            0.03f, -0.08f, 0.0f,   // bottom left
            0.03f,  0.08f, 0.0f,    // top left
            -0.03f,  0.08f, 0.0f}; // top right


    private static final float COLOR[] = { 0.0f, 0.0f, 0.0f, 1.0f };

    private static final int NUMBER_POINTS = 6;

    private static final float INITIAL_COORDINATE_X = -1.55f;

    private static final float INITIAL_COORDINATE_Y = -.9f;

    private static final float INITIAL_ANGLE = 0.0f;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public PauseButton() {
        super(COLOR, NUMBER_POINTS, INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y, INITIAL_ANGLE );
        super.setmCoordinates(pauseCoords);
        super.initializeVertexBuffer();
    }
}