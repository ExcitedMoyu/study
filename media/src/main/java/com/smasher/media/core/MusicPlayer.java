package com.smasher.media.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;


import java.io.IOException;

/**
 * @author matao
 * @date 2019/5/25
 */
public class MusicPlayer extends CorePlayer {

    private static final String TAG = "AudioPlayer";


    public MusicPlayer(Context context, MediaSessionCompat session) {
        super(context, session);
    }

    @Override
    protected void reset() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
    }

    @Override
    protected void setDataSource(Uri uri) throws IOException {
        if (mPlayer != null) {
            Log.d(TAG, "setDataSource: " + uri.toString());
            mPlayer.setDataSource(uri.toString());
        }
    }

    @Override
    protected void prepare() throws IOException {
        if (mPlayer != null) {
            mPlayer.prepare();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_CONNECTING);
        }
    }

    @Override
    protected void start() {
        if (mPlayer != null) {
            mPlayer.start();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    @Override
    protected void play() {
        if (mPlayer != null) {
            mPlayer.start();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    @Override
    protected void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_PAUSED);

        }
    }

    @Override
    protected void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_STOPPED);
        }
    }

    @Override
    protected void onPausing() {
        if (mPlayer != null) {

        }

    }

    @Override
    protected void onShutDownPausing() {
        if (mPlayer != null) {

        }
    }


    @Override
    protected boolean isPlaying() {
        boolean isPlaying = false;
        if (mPlayer != null) {
            isPlaying = mPlayer.isPlaying();
        }
        return isPlaying;
    }


    @Override
    protected long getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    @Override
    protected long getCurrTime() {
        if (mPlayer != null) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    protected long seek(int pos) {
        if (mPlayer != null) {
            mPlayer.seekTo(pos);
            return pos;
        }
        return 0;
    }

    @Override
    protected long getBufferLen() {
        return 100;
    }

    @Override
    protected long getTotalLen() {
        return 100;
    }


    @Override
    protected int getBufferPercent() {
        return 100;
    }

    @Override
    protected void onPreparedLogic(MediaPlayer mp) {
        Log.d(TAG, "onPreparedLogic: ");
        mPlaybackManager.setState(PlaybackStateCompat.STATE_PAUSED);
        mPlayer.start();
        mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
    }

    @Override
    protected void onCompletionLogic(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletionLogic: ");
        mPlayer.reset();
        mPlaybackManager.setState(PlaybackStateCompat.STATE_NONE);
    }

    @Override
    protected void onBufferingUpdateLogic(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdateLogic: ");
    }


    private boolean prepare(String mPlayUri) {
        try {
            if (mPlayer == null) {
                return false;
            }

            mPlayer.reset();
            mPlayer.setDataSource(mPlayUri);
            mPlayer.prepare();
            mIsInitialized = true;
        } catch (IOException e) {
            mIsInitialized = false;
            e.printStackTrace();
        }

        return mIsInitialized;
    }

}
