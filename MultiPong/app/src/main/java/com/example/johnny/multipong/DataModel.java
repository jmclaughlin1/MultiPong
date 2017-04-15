package com.example.johnny.multipong;

import android.content.IntentFilter;

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
        max_height = 0;
        max_width = 0;
        ball_radius = 0;
        paddle_height = 0;
        paddle_width = 0;
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
}
