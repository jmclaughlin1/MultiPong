package com.example.johnny.multipong.shapes;

/**
 * Created by Jason Esquivel on 4/8/2017.
 */

public class ResumeButton extends Shape{


    private static final float resumeCoords[] = {
            0.0f, 0.0f, 0.0f,     // center
            0.06f,  0.08f, 0.0f,   // top right
            0.06f, -0.08f, 0.0f,   // bottom right
            -0.06f, 0.0f, 0.0f,   // bottom left
            0.06f,  0.08f, 0.0f}; // top right


    private static final float COLOR[] = { 0.0f, 0.0f, 0.0f, 1.0f };

    private static final int NUMBER_POINTS = 5;

    private static final float INITIAL_COORDINATE_X = -1.55f;

    private static final float INITIAL_COORDINATE_Y = -.9f;

    private static final float INITIAL_ANGLE = 0.0f;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public ResumeButton() {
        super(COLOR, NUMBER_POINTS, INITIAL_COORDINATE_X, INITIAL_COORDINATE_Y, INITIAL_ANGLE );
        super.setmCoordinates(resumeCoords);
        super.initializeVertexBuffer();
    }
}