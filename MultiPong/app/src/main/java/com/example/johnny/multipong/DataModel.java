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

    private int max_width;

    private int max_height;

    private int paddle_width;

    private int paddle_height;

    private int ball_radius;

    private int paddle_x, paddle_y, paddle_theta;

    private int ball_x;

    private int ball_y;

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

            ball_radius = max_width / BALL_RADIUS_RATIO;

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
        paddle_theta = 0;
        paddle_x = 0;
        paddle_y = 0;
        ball_x = (max_width / 2) - ball_radius;
        ball_y = max_height;
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

        PositionTask positionTask = new PositionTask();

        positionTimer.scheduleAtFixedRate(positionTask, 0, 50);
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

    private class PositionTask extends TimerTask {

        @Override
        public void run() {
            paddle_x = (max_width / 2) - (paddle_width / 2);
            paddle_y = paddle_height * 3;
            paddle_theta = 0;

            ball_y = (ball_y - 10) % max_height;

            sendPositionMessage();
        }
    }
}
