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
    private MediaPlayer bgMediaPlayer;
    private MediaPlayer sfxMediaPlayer;
    private int bgPos;

    private static final float MAX_VOLUME = 100.f;
    //@Override
    //public IBinder onBind(Intent intent) {
    //    return null;
    //}

    @Override
    public void runService() {
        //super.onCreate();
        Log.i(TAG, "Sound Service Started");
        bgMediaPlayer = bgMediaPlayer.create(this, R.raw.spystory);
        bgMediaPlayer.setOnErrorListener(this);

        if(bgMediaPlayer!=null){
            bgMediaPlayer.setLooping(true);
            bgMediaPlayer.setVolume(0.5f,0.5f);
        }
        bgMediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mp, what, extra);
                return true;
            }
        });

        sfxMediaPlayer = sfxMediaPlayer.create(this, R.raw.button31);
        sfxMediaPlayer.setOnErrorListener(this);

        if(sfxMediaPlayer!=null){
            sfxMediaPlayer.setVolume(0.5f,0.5f);
        }
        sfxMediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mp, what, extra);
                return true;
            }
        });

        bgMediaPlayer.start();
    }

    @Override
    public void processServiceMessage(String id, int[] body) {
        if(id.equals(Messages.MusicMessage.BACKGROUND_MUSIC_VOLUME_ID)){
            Log.i(TAG, "Background Music Volume Message");
            float backgroundVolume = (float)body[Messages.MusicMessage.BACKGROUND_VOLUME]/MAX_VOLUME;
            bgMediaPlayer.setVolume(backgroundVolume, backgroundVolume);
        }
        else if(id.equals(Messages.MusicMessage.BACKGROUND_MUSIC_PAUSE_ID)){
            Log.i(TAG, "Background Music Pause Message");
            pauseBackgroundMusic();
        }
        else if(id.equals(Messages.MusicMessage.BACKGROUND_MUSIC_RESUME_ID)){
            Log.i(TAG, "Background Music Resume Message");
            resumeBackgroundMusic();
        }
        else if(id.equals(Messages.MusicMessage.BACKGROUND_MUSIC_STOP_ID)){
            Log.i(TAG, "Background Music Stop Message");
            stopBackgroundMusic();
        }
        else if(id.equals(Messages.MusicMessage.SFX_MUSIC_VOLUME_ID)){
            Log.i(TAG, "SFX Music Volume Message");
            float sfxVolume = (float)body[Messages.MusicMessage.SFX_VOLUME]/MAX_VOLUME;
            sfxMediaPlayer.setVolume(sfxVolume, sfxVolume);
        }
        else if(id.equals(Messages.MusicMessage.SFX_MUSIC_PLAY_ID)){
            Log.i(TAG, "SFX Music Play Message");
            sfxMediaPlayer.start();
        }
    }

    @Override
    public IntentFilter getValidServiceMessages() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Messages.MusicMessage.BACKGROUND_MUSIC_VOLUME_ID);
        intentFilter.addAction(Messages.MusicMessage.BACKGROUND_MUSIC_PAUSE_ID);
        intentFilter.addAction(Messages.MusicMessage.BACKGROUND_MUSIC_RESUME_ID);
        intentFilter.addAction(Messages.MusicMessage.BACKGROUND_MUSIC_STOP_ID);
        intentFilter.addAction(Messages.MusicMessage.SFX_MUSIC_VOLUME_ID);
        intentFilter.addAction(Messages.MusicMessage.SFX_MUSIC_PLAY_ID);
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

        Log.i(TAG, "onDestroy");
        if(bgMediaPlayer != null)
        {
            stopBackgroundMusic();
        }
        super.onDestroy();
    }

    public void pauseBackgroundMusic()
    {
        if(bgMediaPlayer.isPlaying()){
            bgMediaPlayer.pause();
            bgPos = bgMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeBackgroundMusic()
    {
        if(bgMediaPlayer.isPlaying()==false){
            bgMediaPlayer.seekTo(bgPos);
            bgMediaPlayer.start();
        }
    }

    public void stopBackgroundMusic()
    {
        bgMediaPlayer.stop();
        bgMediaPlayer.release();
        bgMediaPlayer = null;
    }

}
