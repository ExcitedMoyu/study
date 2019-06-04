package com.smasher.media.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.smasher.media.helper.AudioFocusHelper;
import com.smasher.media.helper.NotificationHelper;
import com.smasher.media.helper.TelephonyHelper;
import com.smasher.media.manager.PlaybackManager;

import java.io.IOException;


/**
 * @author moyu
 */
public abstract class CorePlayer extends PhoneStateListener implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "CorePlayer";

    Context mContext;
    MediaPlayer mPlayer;
    PlaybackManager mPlaybackManager;

    NotificationHelper mNotificationHelper;

    AudioFocusHelper mAudioFocusHelper;
    TelephonyHelper mTelephonyHelper;


    /**
     * 是否因失去焦点暂停
     */
    boolean mPausedByTransientLossOfFocus;


    /**
     * 手机状态监听器：来电话、来短信事件
     */
    boolean mResumeAfterCall = false;


    public CorePlayer(Context context, MediaSessionCompat session) {

        mContext = context;

        mAudioFocusHelper = new AudioFocusHelper(this);
        mTelephonyHelper = new TelephonyHelper(mContext);
        mTelephonyHelper.setListener(this);


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

    protected abstract void setNotificationHelper(NotificationHelper notificationHelper);

    protected abstract void reset();

    protected abstract void setDataSource(Uri uri) throws IOException;

    protected abstract void prepare() throws IOException;

    protected abstract void start();

    protected abstract void play();

    protected abstract void pause();

    protected abstract void stop();

    protected abstract void skipToPrevious(Uri uri) throws IOException;

    protected abstract void skipToNext(Uri uri) throws IOException;

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


    protected abstract void onAudioFocusChangeImp(int focusChange);

    protected abstract void onCallStateChangedImp(int state, String phoneNumber);


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


    @Override
    public void onAudioFocusChange(int focusChange) {
        onAudioFocusChangeImp(focusChange);
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        onCallStateChangedImp(state, phoneNumber);
    }
}
