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
    @Override
    public void processServiceMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidServiceMessages() {
        return null;
    }

    @Override
    public void runService() {

    }
}
