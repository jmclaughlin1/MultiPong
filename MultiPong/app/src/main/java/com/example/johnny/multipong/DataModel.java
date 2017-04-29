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

    private int center_accel_x, center_accel_y, left_accel_x, right_accel_x, left_accel_y, right_accel_y;

    private int ball_x;

    private int ball_y;

    private double accel_pixel_ratio_x, accel_pixel_ratio_y;

    private int ball_y_increment, ball_x_increment, paddle_x_increment;

    private double[][] paddle_rotation_matrix;

    private boolean ball_x_direction;

    private boolean player1;

    private int player1_score, player2_score;

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

            if (player1) {
                paddle_x_increment = ball_x_increment = max_width / 50;
                ball_x = (max_width / 2);
                ball_x_increment = 0;
                ball_y = max_height;
            }
            requestCenterPosition();

        } else if (id.equals(Messages.CenterPositionMessage.CENTER_POSITION_MESSAGE_ID)) {
            center_accel_x = body[Messages.CenterPositionMessage.CENTER_X_FIELD];
            center_accel_y = body[Messages.CenterPositionMessage.CENTER_Y_FIELD];
            left_accel_x = body[Messages.CenterPositionMessage.LEFT_X_FIELD];
            right_accel_x = body[Messages.CenterPositionMessage.RIGHT_X_FIELD];
            left_accel_y = body[Messages.CenterPositionMessage.LEFT_Y_FIELD];
            right_accel_x = body[Messages.CenterPositionMessage.RIGHT_Y_FIELD];

            accel_pixel_ratio_y = max_width / right_accel_y;
            accel_pixel_ratio_x = 120 / left_accel_x;

            PositionTask positionTask = new PositionTask();

            positionTimer.scheduleAtFixedRate(positionTask, 0, 33);
            sendInitMessage();
        } else if (id.equals(Messages.GyroscopeMessage.GYROSCOPE_MESSAGE_ID)) {
            int accel_x = body[Messages.GyroscopeMessage.GYROSCOPE_X];
            int accel_y = body[Messages.GyroscopeMessage.GYROSCOPE_Y];

            paddle_x = (int)(accel_y * accel_pixel_ratio_y);
            paddle_theta = (int)(accel_x * accel_pixel_ratio_x);
        } else if (id.equals(Messages.BallTransferBTMessage.BALL_TRANSFER_MESSAGE_BT_ID)) {
            ball_y_increment = -ball_y_increment;
            ball_y = body[Messages.BallTransferBTMessage.BALL_Y];
            ball_x = body[Messages.BallTransferBTMessage.BALL_X];
            ball_x_increment = body[Messages.BallTransferBTMessage.BALL_ANGLE];
        } else if (id.equals(Messages.UpdateScoreBTMessage.UPDATE_SCORE_MESSAGE_ID)) {
            player1_score = body[Messages.UpdateScoreBTMessage.PLAYER_1_SCORE];
            player2_score = body[Messages.UpdateScoreBTMessage.PLAYER_2_SCORE];

            ball_y_increment = max_height / 75;
            ball_x = (max_width/2);
            ball_x_increment = 0;
            ball_y = max_height;
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
        player1_score = player2_score = 0;
        player1 = false;

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

    private void requestCenterPosition() {
        publishServiceMessage(Messages.CenterPositionMessage.CENTER_POSITION_MESSAGE_ID, null);
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

    private void updateScore() {
        if (player1) {
            player1_score++;
        } else {
            player2_score++;
        }

        int body[] = new int[Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_SIZE];
        body[Messages.UpdateScoreMessage.PLAYER_1_SCORE] = player1_score;
        body[Messages.UpdateScoreMessage.PLAYER_2_SCORE] = player2_score;

        publishServiceMessage(Messages.UpdateScoreMessage.UPDATE_SCORE_MESSAGE_ID, body);
    }

    private void sendBallTransferMessage() {
        int body[] = new int[Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_SIZE];
        body[Messages.BallTransferMessage.BALL_ANGLE] = ball_x_increment;
        body[Messages.BallTransferMessage.BALL_X] = ball_x;
        body[Messages.BallTransferMessage.BALL_Y] = ball_y;

        publishServiceMessage(Messages.BallTransferMessage.BALL_TRANSFER_MESSAGE_ID, body);
    }

    private class PositionTask extends TimerTask {

        @Override
        public void run() {
            boolean hit_right_wall = ball_x > max_width;
            boolean hit_left_wall = ball_x < 0;
            boolean hit_top_wall = ball_y < 0;
            boolean hit_bottom_wall = ball_y > max_height;

            boolean hit_paddle = (ball_y <= (paddle_y + (paddle_height)))
                    && ball_x >= (paddle_x - (paddle_width/2))
                    && ball_x <= (paddle_x + (paddle_width/2));

            if (hit_paddle) {
                if (paddle_theta % 360 != 0) {
                    int slope = (int) Math.atan(paddle_theta);
                    if (slope != 0) ball_x_increment = -(ball_y_increment / slope);
                }

                ball_y_increment = -(ball_y_increment + BALL_SPEED);

            } else if (hit_left_wall) {
                ball_x = 0;
                ball_x_increment = -ball_x_increment;
            } else if (hit_right_wall) {
                ball_x = max_width;
                ball_x_increment = -ball_x_increment;
            } else if (hit_bottom_wall) {
                updateScore();
            } else if (hit_top_wall) {
                sendBallTransferMessage();
            }

            ball_x -= ball_x_increment;
            ball_y -= ball_y_increment;

            sendPositionMessage();
        }
    }
}
