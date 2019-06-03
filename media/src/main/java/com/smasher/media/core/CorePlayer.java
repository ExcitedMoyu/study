package com.smasher.media.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.smasher.media.manager.PlaybackManager;

import java.io.IOException;


/**
 * @author moyu
 */
public abstract class CorePlayer implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener {

    PlaybackManager mPlaybackManager;
    MediaPlayer mPlayer;
    Context mContext;
    boolean mIsInitialized;
    AudioFocusHelper mAudioFocusHelper;


    public CorePlayer(Context context, MediaSessionCompat session) {

        mContext = context;

        mAudioFocusHelper = new AudioFocusHelper();
        mPlaybackManager = PlaybackManager.getInstance();
        mPlaybackManager.setMediaSession(session);
        mPlaybackManager.setState(PlaybackStateCompat.STATE_NONE);

        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnPreparedListener(this);
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

    protected abstract void reset();

    protected abstract void setDataSource(Uri uri) throws IOException;

    protected abstract void prepare() throws IOException;

    protected abstract void start();

    protected abstract void play();

    protected abstract void pause();

    protected abstract void stop();

    protected abstract boolean isPlaying();

    protected abstract void onPausing();

    protected abstract void onShutDownPausing();

    protected abstract long getDuration();

    protected abstract long getCurrTime();

    protected abstract long seek(int pos);

    protected abstract long getBufferLen();

    protected abstract long getTotalLen();

    protected abstract int getBufferPercent();


    protected abstract void onPreparedLogic(MediaPlayer mp);

    protected abstract void onCompletionLogic(MediaPlayer mp);

    protected abstract void onBufferingUpdateLogic(MediaPlayer mp, int percent);


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

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        onBufferingUpdateLogic(mp, percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        onPreparedLogic(mp);
    }


    //region state

    public int getState() {
        return mPlaybackManager.getState().getState();
    }


    //endregion
}
