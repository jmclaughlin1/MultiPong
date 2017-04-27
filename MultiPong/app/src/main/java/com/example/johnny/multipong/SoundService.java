package com.example.johnny.multipong;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

/**
 * Created by Jason Esquivel on 4/27/2017.
 */

public class SoundService extends BaseService implements MediaPlayer.OnErrorListener {
    String TAG = "SoundService";
    private MediaPlayer mediaPlayer;

    //@Override
    //public IBinder onBind(Intent intent) {
    //    return null;
    //}

    @Override
    public void runService() {
        //super.onCreate();
        Log.i(TAG, "Sound Service Started");
        mediaPlayer = mediaPlayer.create(this, R.raw.spystory);
        mediaPlayer.setOnErrorListener(this);

        if(mediaPlayer!=null){
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f,1.0f);
        }
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mp, what, extra);
                return true;
            }
        });
        mediaPlayer.start();
    }

    @Override
    public void processServiceMessage(String id, int[] body) {

    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFilter = new IntentFilter();
        return intentFilter;
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(mp != null)
        {
            try {
                mp.stop();
                mp.release();
            }finally {
                mp = null;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
        {
            try{
                mediaPlayer.stop();
                mediaPlayer.release();
            }finally {
                mediaPlayer = null;
            }
        }
    }
    
}
