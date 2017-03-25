package com.example.johnny.commonframeworkmodule;

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
}
