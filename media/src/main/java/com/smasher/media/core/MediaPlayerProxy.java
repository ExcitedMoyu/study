package com.smasher.media.core;

import android.content.Context;


/**
 * Created on 2019/5/29.
 *
 * @author moyu
 */
public class MediaPlayerProxy {
    private static final String TAG = "MediaPlayerProxy";
    private CorePlayer mPlayer = null;

    public MediaPlayerProxy() {
    }


    public boolean initPlay(Context context) {
        boolean initOk = false;
        try {
            if (mPlayer != null) {
                mPlayer.onStop();
            }
            mPlayer = new MusicPlayer(context);
            initOk = mPlayer.onPrepare();
        } catch (Exception e) {
            e.printStackTrace();
            initOk = false;
        }
        return initOk;
    }


    public void onPrepare(String uri) {
        if (mPlayer != null) {
            mPlayer.onPrepare(uri);
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
            mPlayer.onPlay();
        }
    }

    public void stop() {
        if (mPlayer != null) {
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
//            int realDuration = (mRealDuration + 1) * 1000;
//            if (realDuration > duration) {
//                return mRealDuration * 1000;
//            } else {
//                return mPlayer.getDuration();
//            }
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
}
