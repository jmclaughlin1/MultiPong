package com.example.johnny.multipong;

import android.content.IntentFilter;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This service performs all the data process and number crunching for the game. It models the
 * in-game physics and provides the UI thread all the necessary data to render the game on the
 * phone.
 *
 * Created by John McLaughlin and Onkar on 4/9/17.
 */
public class DataModel extends BaseService {

    private static final int PADDLE_WIDTH_RATIO = 4;

    private static final int PADDLE_HEIGHT_RATIO = 12;

    private static final int BALL_RADIUS_RATIO = 50;

    private static final int BALL_SPEED = 1;

    private int max_width;

    private int max_height;

    private int paddle_width;

    private int paddle_height;

    private int ball_radius;

    private int paddle_x, paddle_y, paddle_theta;

    private int ball_x;

    private int ball_y;

    private int ball_y_increment, ball_x_increment, paddle_x_increment;

    private double[][] paddle_rotation_matrix;

    private boolean ball_x_direction;

    private Timer positionTimer;

    public DataModel() {
        Log.i("Data Model", "Data Model is constructed!");
        positionTimer = new Timer();
    }

    @Override
    public void processServiceMessage(String id, int[] body) {
        if (id.equals(Messages.ScreenResMessage.SCREEN_RES_MESSAGE_ID)) {
            max_width = body[Messages.ScreenResMessage.SCREEN_WIDTH];
            max_height = body[Messages.ScreenResMessage.SCREEN_HEIGHT];

            paddle_width = max_width / PADDLE_WIDTH_RATIO;
            paddle_height = max_height / PADDLE_HEIGHT_RATIO;
            paddle_x = (max_width / 2);

            ball_radius = max_width / BALL_RADIUS_RATIO;
            ball_y_increment = max_height / 75;
            paddle_x_increment = ball_x_increment = max_width / 50;
            ball_x = (max_width/2);
            ball_x_increment = 0;
            ball_y = max_height;

            PositionTask positionTask = new PositionTask();

            positionTimer.scheduleAtFixedRate(positionTask, 0, 33);
            sendInitMessage();
        }
    }

    @Override
    public IntentFilter getValidServiceMessages()
    {
        IntentFilter messageIds = new IntentFilter();
        messageIds.addAction(Messages.ScreenResMessage.SCREEN_RES_MESSAGE_ID);

        return messageIds;
    }

    @Override
    public void runService() {
        Log.i("Data Model", "Running Data Model Service!");
        max_height = 0;
        max_width = 0;
        ball_radius = 0;
        paddle_height = 0;
        paddle_width = 0;
        paddle_theta = 30;
        paddle_x = 0;
        paddle_y = 100;
        ball_x = 200;
        ball_y = max_height;
        ball_x_direction = false;

        paddle_rotation_matrix = new double[2][2];
    }

    /**
     * Sends an Init message containing the ball and paddle dimensions.
     */
    private void sendInitMessage() {
        int body[] = new int[Messages.InitMessage.INIT_MESSAGE_SIZE];

        body[Messages.InitMessage.BALL_RADIUS] = ball_radius;
        body[Messages.InitMessage.PADDLE_HEIGHT] = paddle_height;
        body[Messages.InitMessage.PADDLE_WIDTH] = paddle_width;

        publishServiceMessage(Messages.InitMessage.INIT_MESSAGE_ID, body);


    }

    private void sendPositionMessage() {
        int body[] = new int[Messages.PositionMessage.POSITION_MESSAGE_SIZE];

        body[Messages.PositionMessage.BALL_X] = ball_x;
        body[Messages.PositionMessage.BALL_Y] = ball_y;
        body[Messages.PositionMessage.DRAW_BALL] = 1;
        body[Messages.PositionMessage.PADDLE_ANGLE] = paddle_theta;
        body[Messages.PositionMessage.PADDLE_X] = paddle_x;
        body[Messages.PositionMessage.PADDLE_Y] = paddle_y;

        publishServiceMessage(Messages.PositionMessage.POSITION_MESSAGE_ID, body);
    }

    boolean paddle_test = false;

    private class PositionTask extends TimerTask {

        @Override
        public void run() {
            boolean hit_right_wall = ball_x > max_width;
            boolean hit_left_wall = ball_x < 0;
            boolean hit_top_wall = ball_y < 0;
            boolean hit_bottom_wall = ball_y > max_height;

            // Test purposes only. Paddle theta will be controlled by accelerometer.
            if (paddle_theta >= 60) paddle_test = true;
            else if (paddle_theta <= -60) paddle_test = false;
            paddle_theta = paddle_test ? paddle_theta - 1 : paddle_theta + 1;

            //paddle_rotation_matrix[0][0] = paddle_rotation_matrix[1][1] = Math.cos(paddle_theta);
            //paddle_rotation_matrix[0][1] = Math.sin(paddle_theta);
            //paddle_rotation_matrix[1][0] = -(Math.sin(paddle_theta));

            boolean hit_paddle = (ball_y <= (paddle_y + (paddle_height)))
                    && ball_x >= (paddle_x - (paddle_width/2))
                    && ball_x <= (paddle_x + (paddle_width/2));



            if (hit_paddle) {
                if (paddle_theta % 360 != 0) {
                    int slope = (int) Math.atan(paddle_theta);
                    if (slope != 0) ball_x_increment = -(ball_y_increment / slope);
                }

                //ball_y = ball_y_increment > 0 ?
                ball_y_increment = -(ball_y_increment + BALL_SPEED);

            } else if (hit_left_wall) {
                ball_x = 0;
                ball_x_increment = -ball_x_increment;
            } else if (hit_right_wall) {
                ball_x = max_width;
                ball_x_increment = -ball_x_increment;
            } else if (hit_bottom_wall) {
                ball_y = max_height;
                ball_y_increment = -ball_y_increment;
            } else if (hit_top_wall) {
                ball_y = 0;
                ball_y_increment = -ball_y_increment;
            }
            /*if (hit_top_bottom_walls || hit_paddle) ball_y_increment = -ball_y_increment;

            if (hit_side_walls) ball_x_increment = -ball_x_increment;
            else if (hit_paddle && paddle_theta % 360 != 0) {
                int slope = (int) Math.atan(paddle_theta);
                ball_x_increment = -(ball_y_increment / slope);
            }*/

            // TODO: Control paddle_x with accelerometer data

            ball_x -= ball_x_increment;
            ball_y -= ball_y_increment;

            sendPositionMessage();
        }
    }
}
