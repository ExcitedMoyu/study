package com.smasher.music.core;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.smasher.music.constant.PlayerState;
import com.smasher.music.entity.MediaInfo;
import com.smasher.music.listener.PlayerListener;

/**
 * @author moyu
 */
public abstract class CorePlayer extends Thread {

    protected MediaPlayer mPlayer;
    protected MediaInfo mMediaInfo;
    protected AudioAttributes mAudioAttributes;
    protected Builder mBuilder;
    protected Context mContext;

    private PlayerListener mListener;

    protected boolean mIsInitialized;
    protected PlayerState mPlayState = PlayerState.PLAY_STATE_PLAY;


    public CorePlayer(Context context, MediaInfo mediaInfo) {

        mContext = context;
        mMediaInfo = mediaInfo;

        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setOnErrorListener(mErrorListener);
        mPlayer.setOnCompletionListener(mCompletionListener);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBuilder = new AudioAttributes.Builder();
            mBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            mBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            mBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            mBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            mAudioAttributes = mBuilder.build();
            mPlayer.setAudioAttributes(mAudioAttributes);
        }
    }


    public void setListener(PlayerListener listener) {
        mListener = listener;
    }


    protected final void notifyEvent(int what, int subwhat, Object ex) {
        if (mListener != null) {
            mListener.notifyEvent(what, subwhat, ex);
        }

    }

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

    protected void setState(PlayerState state) {
        mPlayState = state;
    }

    protected PlayerState getPlayState() {
        return mPlayState;
    }

    protected abstract boolean onPrepare();

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

    private MediaPlayer.OnCompletionListener mCompletionListener = this::onCompletionLogic;

    private MediaPlayer.OnErrorListener mErrorListener = (mp, what, extra) -> {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                notifyEvent(PlayerListener.PLAY_EVENT_ERROR, 0, mMediaInfo);
                return true;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                break;
            default:
                break;
        }
        return false;
    };


}
