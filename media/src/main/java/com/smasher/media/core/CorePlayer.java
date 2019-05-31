package com.smasher.media.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;


/**
 * @author moyu
 */
public abstract class CorePlayer extends Thread implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    MediaPlayer mPlayer;
    Context mContext;
    boolean mIsInitialized;

//    private PlayerListener mListener;

    public CorePlayer(Context context) {

        mContext = context;

        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
    }


//    public void setListener(PlayerListener listener) {
//        mListener = listener;
//    }



    protected void setVolume(float vol) {
        try {
            mPlayer.setVolume(vol, vol);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected boolean isInitialized() {
        return mIsInitialized;
    }

    protected abstract boolean onPrepare(String uri);

    protected abstract void onPlay();

    protected abstract void onPause();

    protected abstract void onPausing();

    protected abstract void onResume();

    protected abstract void onShutDownPausing();

    protected abstract void onStop();

    protected abstract long getDuration();

    protected abstract long getCurrTime();

    protected abstract long seek(int pos);

    protected abstract long getBufferLen();

    protected abstract long getTotalLen();

    protected abstract boolean isPlaying();

    protected abstract int getBufferPercent();

    protected abstract void onCompletionLogic(MediaPlayer mp);


    @Override
    public void onCompletion(MediaPlayer mp) {
        onCompletionLogic(mp);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                return true;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                break;
            default:
                break;
        }
        return false;
    }
}
