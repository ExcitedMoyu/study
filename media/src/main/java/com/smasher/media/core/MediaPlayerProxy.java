package com.smasher.media.core;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.smasher.media.manager.PlaybackManager;


/**
 * Created on 2019/5/29.
 *
 * @author moyu
 */
public class MediaPlayerProxy {
    private static final String TAG = "MediaPlayerProxy";
    private CorePlayer mPlayer = null;
    private PlaybackManager mPlaybackManager;
    private PlaybackStateCompat mState;
    private MediaSessionCompat mSession;

    public MediaPlayerProxy(MediaSessionCompat session) {
        mSession = session;
        mPlaybackManager = PlaybackManager.getInstance();
        mPlaybackManager.setState(mSession, PlaybackStateCompat.STATE_NONE);
    }


    public boolean initPlay(Context context) {
        boolean initOk = false;
        try {
            if (mPlayer != null) {
                mPlayer.onStop();
            }
            mPlayer = new MusicPlayer(context);
        } catch (Exception e) {
            e.printStackTrace();
            initOk = false;
        }
        return initOk;
    }


    public void onPrepare(QueueItem currentMusic) {
        if (mPlayer != null) {
            String path = currentMusic.getDescription().getMediaUri().toString();
            Log.d(TAG, "onPrepare: " + path);
            mPlayer.onPrepare(path);
        }
    }


    public void close() {
        if (mPlayer != null) {
            mPlayer.onStop();
            mPlayer = null;
        }
    }

    public void play() {
        if (mPlayer != null) {
            Log.d(TAG, "play: ");
            mPlayer.onPlay();
        }
    }

    public void stop() {
        if (mPlayer != null) {
            Log.d(TAG, "stop: ");
            mPlayer.onStop();
        }
    }

    public void pausing() {
        if (mPlayer != null) {
            mPlayer.onPausing();
        }
    }

    public void pause() {
        if (mPlayer != null) {
            Log.d(TAG, "pause: ");
            mPlayer.onPause();
        }
    }

    public void shutDownPausing() {
        if (mPlayer != null) {
            mPlayer.onShutDownPausing();
        }
    }

    public void resume() {
        if (mPlayer != null) {
            mPlayer.onResume();
        }
    }


    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    public long getDuration() {
        if (mPlayer != null) {
            long duration = mPlayer.getDuration();
        }
        return 0;
    }

    public long getCurrTime() {
        if (mPlayer != null) {
            return mPlayer.getCurrTime();
        }
        return 0;
    }

    public int getBufferPercent() {
        if (mPlayer != null) {
            return mPlayer.getBufferPercent();
        }
        return 0;
    }

    public long seek(int pos) {
        if (mPlayer != null) {
            return mPlayer.seek(pos);
        }
        return 0;
    }

    public long getBufferLen() {
        if (mPlayer != null) {
            return mPlayer.getBufferLen();
        }
        return 0;
    }

    public long getTotalLen() {
        if (mPlayer != null) {
            return mPlayer.getTotalLen();
        }
        return 0;
    }

    public void setVolume(float vol) {
        if (mPlayer != null) {
            mPlayer.setVolume(vol);
        }
    }


    private interface IPathSuccessListener {
        void onSuccess();
    }


    //region state

    public PlaybackStateCompat getState() {
        return mState;
    }


    //endregion
}
