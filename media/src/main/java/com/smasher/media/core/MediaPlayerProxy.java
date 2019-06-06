package com.smasher.media.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.smasher.media.helper.NotificationHelper;

import java.io.IOException;


/**
 * Created on 2019/5/29.
 *
 * @author moyu
 */
public class MediaPlayerProxy extends CorePlayer {
    private static final String TAG = "MediaPlayerProxy";
    private CorePlayer mPlayer = null;


    public MediaPlayerProxy(Context context, MediaSessionCompat session) {
        super(context, session);
        mPlayer = new MusicPlayer(context, session);
    }

    @Override
    public void setCompleteListener(CompleteListener listener) {
        mCompleteListener = listener;
        if (mPlayer != null) {
            mPlayer.setCompleteListener(mCompleteListener);
        }
    }




    @Override
    public void reset() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
    }

    @Override
    public void setDataSource(Uri uri) throws IOException {
        if (mPlayer != null) {
            mPlayer.setDataSource(uri);
        }
    }

    @Override
    public void prepare() throws IOException {
        if (mPlayer != null) {
            Log.d(TAG, "prepare: ");
            mPlayer.prepare();

        }
    }

    @Override
    public void start() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mPlayer != null) {
            Log.d(TAG, "pause: ");
            mPlayer.pause();
        }
    }

    @Override
    public void skipToPrevious(Uri uri) throws IOException {
        if (mPlayer != null) {
            Log.d(TAG, "skipToPrevious: ");
            mPlayer.skipToPrevious(uri);
        }
    }

    @Override
    public void skipToNext(Uri uri) throws IOException {
        if (mPlayer != null) {
            Log.d(TAG, "skipToNext: ");
            mPlayer.skipToNext(uri);
        }
    }

    @Override
    public void stop() {
        if (mPlayer != null) {
            Log.d(TAG, "stop: ");
            mPlayer.stop();
        }
    }


    @Override
    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }


    @Override
    public long getDuration() {
        if (mPlayer != null) {
            long duration = mPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public long getCurrTime() {
        if (mPlayer != null) {
            return mPlayer.getCurrTime();
        }
        return 0;
    }

    @Override
    public int getBufferPercent() {
        if (mPlayer != null) {
            return mPlayer.getBufferPercent();
        }
        return 0;
    }

    @Override
    public long seek(int pos) {
        if (mPlayer != null) {
            return mPlayer.seek(pos);
        }
        return 0;
    }

    @Override
    public long getBufferLen() {
        if (mPlayer != null) {
            return mPlayer.getBufferLen();
        }
        return 0;
    }

    @Override
    public long getTotalLen() {
        if (mPlayer != null) {
            return mPlayer.getTotalLen();
        }
        return 0;
    }

    @Override
    public void setVolume(float vol) {
        if (mPlayer != null) {
            mPlayer.setVolume(vol);
        }
    }

    //region


    @Override
    protected void onPreparedLogic(MediaPlayer mp) {

    }

    @Override
    protected void onCompletionLogic(MediaPlayer mp) {

    }

    @Override
    protected void onBufferingUpdateLogic(MediaPlayer mp, int percent) {

    }

    @Override
    protected void onAudioFocusChangeImp(int focusChange) {

    }

    @Override
    protected void onCallStateChangedImp(int state, String phoneNumber) {

    }


    @Override
    public void setNotificationHelper(NotificationHelper notificationHelper) {
        if (mPlayer != null) {
            mPlayer.setNotificationHelper(notificationHelper);
        }
    }


    //region state

    @Override
    public int getState() {
        return mPlayer.getState();
    }

    //endregion
}
