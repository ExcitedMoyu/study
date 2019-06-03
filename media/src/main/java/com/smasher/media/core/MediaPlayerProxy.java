package com.smasher.media.core;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import java.io.IOException;


/**
 * Created on 2019/5/29.
 *
 * @author moyu
 */
public class MediaPlayerProxy {
    private static final String TAG = "MediaPlayerProxy";
    private CorePlayer mPlayer = null;


    public MediaPlayerProxy(Context context, MediaSessionCompat session) {
        mPlayer = new MusicPlayer(context, session);
    }


    public void reset() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
    }

    public void setDataSource(Uri uri) throws IOException {
        if (mPlayer != null) {
            mPlayer.setDataSource(uri);
        }
    }


    public void prepare() throws IOException {
        if (mPlayer != null) {
            Log.d(TAG, "prepare: ");
            mPlayer.prepare();

        }
    }


    public void start() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }


    public void play() {
        if (mPlayer != null) {
            Log.d(TAG, "play: ");
            mPlayer.play();
        }
    }


    public void pause() {
        if (mPlayer != null) {
            Log.d(TAG, "pause: ");
            mPlayer.pause();
        }
    }


    public void stop() {
        if (mPlayer != null) {
            Log.d(TAG, "stop: ");
            mPlayer.stop();
        }
    }

    public void pausing() {
        if (mPlayer != null) {
            mPlayer.onPausing();
        }
    }

    public void shutDownPausing() {
        if (mPlayer != null) {
            mPlayer.onShutDownPausing();
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


    //region state

    public int getState() {
        return mPlayer.getState();
    }


    //endregion
}
