package com.smasher.media.core;

import android.content.Context;
import android.media.MediaPlayer;


import java.io.IOException;

/**
 * @author matao
 * @date 2019/5/25
 */
public class MusicPlayer extends CorePlayer {

    private static final String TAG = "AudioPlayer";


    public MusicPlayer(Context context) {
        super(context);
    }



    @Override
    protected boolean onPrepare(String uri) {
        return prepare(uri);
    }


    @Override
    protected void onPlay() {
        if (mPlayer != null) {
            mPlayer.start();
        }

    }

    @Override
    protected void onPause() {
        if (mPlayer != null && mIsInitialized) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onPausing() {
        if (mPlayer != null) {

        }

    }

    @Override
    protected void onResume() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    @Override
    protected void onShutDownPausing() {
        if (mPlayer != null) {
        }
    }

    @Override
    protected void onStop() {
        if (mIsInitialized) {
            mPlayer.release();
            mPlayer = null;
            mIsInitialized = false;
        }
        mContext = null;
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
    protected boolean isPlaying() {
        return false;
    }

    @Override
    protected int getBufferPercent() {
        return 100;
    }

    @Override
    protected void onCompletionLogic(MediaPlayer mediaPlayer) {

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
