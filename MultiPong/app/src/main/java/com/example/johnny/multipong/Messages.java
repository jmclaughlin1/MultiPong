package com.example.johnny.multipong;

/**
 * This class will contain the list of all IDs for messages sent by all the activities and services.
 *
 * @author Johnny McLaughlin
 */

public abstract class Messages {

    public static final String MESSAGE_BODY = "Body";

    public abstract class TestMessage {
        public final static String TEST_MESSAGE_ID = "Test";

        public final static int TEST_MESSAGE_SIZE = 3;

        public final static int TEST_FIELD_1 = 0;
        public final static int TEST_FIELD_2 = 1;
        public final static int TEST_FIELD_3 = 2;
    }

    public abstract class TestMessageResponse {
        public final static String TEST_MESSAGE_ID = "TestResponse";

        public final static int TEST_MESSAGE_SIZE = 3;

        public final static int TEST_FIELD_1 = 0;
        public final static int TEST_FIELD_2 = 1;
        public final static int TEST_FIELD_3 = 2;
    }

    /**
     * Sent from UI thread to Data Model to set the screen resolution of the phone.
     */
    public abstract class ScreenResMessage {
        public static final String SCREEN_RES_MESSAGE_ID = "ScreenRes";

        public static final int SCREEN_WIDTH = 0;
        public static final int SCREEN_HEIGHT = 1;
        public static final int PLAYER_ID = 2;

        public static final int SCREEN_RES_MESSAGE_SIZE = 3;
    }

    /**
     * Send from Data Model to UI thread to set the field size, ball and paddle size.
     */
    public abstract class InitMessage {
        public static final String INIT_MESSAGE_ID = "Init";

        public static final int BALL_RADIUS = 0;
        public static final int PADDLE_HEIGHT = 1;
        public static final int PADDLE_WIDTH = 2;

        public static final int INIT_MESSAGE_SIZE = 3;
    }

    /**
     * Sent from Data Model to UI thread to update the position of the ball and paddle, and change
     * the paddle's angle
     */
    public abstract class PositionMessage {
        public static final String POSITION_MESSAGE_ID = "Position";

        public static final int DRAW_BALL = 0;
        public static final int BALL_X = 1;
        public static final int BALL_Y = 2;
        public static final int PADDLE_X = 3;
        public static final int PADDLE_Y = 4;
        public static final int PADDLE_ANGLE = 5;

        public static final int POSITION_MESSAGE_SIZE = 6;

    }

    /**
     * Sent from Data Model to UI thread to update the score on the screen
     */
    public abstract class UpdateScoreMessage {
        public static final String UPDATE_SCORE_MESSAGE_ID = "Score";

        public static final int PLAYER_1_SCORE = 0;
        public static final int PLAYER_2_SCORE = 1;

        public static final int UPDATE_SCORE_MESSAGE_SIZE = 2;
    }

    public abstract class UpdateScoreBTMessage {
        public static final String UPDATE_SCORE_MESSAGE_ID = "ScoreBT";

        public static final int PLAYER_1_SCORE = 0;
        public static final int PLAYER_2_SCORE = 1;

        public static final int UPDATE_SCORE_MESSAGE_SIZE = 2;
    }

    /**
     * Send from the UI thread to the Bluetooth thread to state established connection
     */
    public abstract class ConnectedBluetoothMessage {
        public static final String CONNECTED_BLUETOOTH_MESSAGE_ID = "ConnectedBluetooth";

        public static final int CONNECTED_BLUETOOTH = 0;

        public static final int CONNECTED_BLUETOOTH_MESSAGE_SIZE = 0;
    }

    /**
     * Sent from the Bluetooth thread to say we have a new player.
     */
    public abstract class NewPlayerMessage {
        public static final String NEW_PLAYER_MESSAGE_ID = "NewPlayer";

        public static final int PLAYER_NAME = 0;
    }

    /**
     * Sent from the Bluetooth thread to say we have disconnected a new player
     */
    public abstract class PlayerRemovedMessage {
        public static final String NEW_PLAYER_MESSAGE_ID = "PlayerRemoved";

        public static final int PLAYER_NAME = 0;
    }

    /**
     * Sent from Bluetooth to Data Model to say the ball should be transferred to the other player
     */
    public abstract class BallTransferBTMessage {
        public static final String BALL_TRANSFER_MESSAGE_BT_ID = "BallTransferBT";

        public static final int BALL_X = 0;
        public static final int BALL_Y = 1;
        public static final int BALL_ANGLE = 2;

        public static final int BALL_TRANSFER_BT_MESSAGE_SIZE = 3;
    }

    /**
     * Sent from Data Model to Bluetooth to the ball has transferred to the player.
     */
    public abstract class BallTransferMessage {
        public static final String BALL_TRANSFER_MESSAGE_ID = "BallTransfer";

        public static final int BALL_X = 0;
        public static final int BALL_Y = 1;
        public static final int BALL_ANGLE = 2;

        public static final int BALL_TRANSFER_MESSAGE_SIZE = 3;
    }

    /**
     * Sent from the Gyroscope thread to the Data Model thread to give the raw data of the
     * gyroscope
     */
    public abstract class GyroscopeMessage {
        public static final String GYROSCOPE_MESSAGE_ID = "Gyroscope";

        public static final int GYROSCOPE_X = 0;
        public static final int GYROSCOPE_Y = 1;
        public static final int GYROSCOPE_Z = 2;

        public static final int GYROSCOPE_MESSAGE_SIZE = 3;
    }

    /**
     * Sent from Bluetooth thread to Data Model and UI threads to pause the game
     */
    public abstract class PauseMessageBT {
        public static final String PAUSE_MESSAGE_BT_ID = "PauseBT";

        public static final int PAUSE_RESUME_FLAG = 0;

        public static final int PAUSE_MESSAGE_BT_SIZE = 1;
    }

    /**
     * Sent from UI thread to Data Model and Bluetooth thread that other player has paused the game
     */
    public abstract class PauseMessage {
        public static final String PAUSE_MESSAGE_ID = "Pause";

        public static final int PAUSE_RESUME_FLAG = 0;

        public static final int PAUSE_MESSAGE_SIZE = 1;
    }

    /**
     * Sent from Bluetooth thread to Data Model and UI thread that other player has won the game
     */
    public abstract class EndGameMessageBT {
        public static final String END_GAME_BT_ID = "EndGameBT";
    }

    /**
     * Sent from Data Model to Bluetooth thread that player has won the game
     */
    public abstract class EndGameMessage {
        public static final String END_GAME_ID = "EndGame";
    }

    /**
     * Sent from any Activity to Sound Service to set background music volume
     */
    public abstract class MusicMessage{

        public static final String BACKGROUND_MUSIC_VOLUME_ID = "BackgroundMusicVolume";
        public static final int BACKGROUND_VOLUME = 0;
        public static final int BACKGROUND_MUSIC_VOLUME_SIZE = 1;

        public static final String BACKGROUND_MUSIC_PAUSE_ID = "BackgroundMusicPause";

        public static final String BACKGROUND_MUSIC_RESUME_ID = "BackgroundMusicResume";

        public static final String BACKGROUND_MUSIC_STOP_ID = "BackgroundMusicStop";

        public static final String SFX_MUSIC_VOLUME_ID = "SFXMusicVolume";
        public static final int SFX_VOLUME = 0;
        public static final int SFX_MUSIC_VOLUME_SIZE = 1;

        public static final String SFX_MUSIC_PLAY_ID = "SFXMusicPlay";
    }

    public abstract class CenterPositionMessage {
        public static final String CENTER_POSITION_MESSAGE_ID = "CenterPosition";

        public static final int CENTER_X_FIELD = 0;
        public static final int CENTER_Y_FIELD = 1;
        public static final int LEFT_X_FIELD = 2;
        public static final int RIGHT_X_FIELD = 3;
        public static final int LEFT_Y_FIELD = 4;
        public static final int RIGHT_Y_FIELD = 5;
        public static final int CENTER_POSITION_MESSAGE_SIZE = 6;
    }

    public abstract class RequestCenterPositionMessage {
        public static final String REQUEST_CENTER_POSITION_MESSAGE_ID = "RequestCenterPosition";
    }
}
