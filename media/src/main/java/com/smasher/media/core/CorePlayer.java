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
        MediaPlayer.OnSeekCompleteListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "CorePlayer";

    Context mContext;

    AudioFocusHelper mAudioFocusHelper;
    CompleteListener mCompleteListener;

    private TelephonyHelper mTelephonyHelper;
    /**
     * 是否因失去焦点暂停
     */
    boolean mPausedByTransientLossOfFocus;

    /**
     * 手机状态监听器：来电话、来短信事件
     */
    boolean mResumeAfterCall = false;


    CorePlayer(Context context, MediaSessionCompat session) {

        mContext = context;
        mAudioFocusHelper = new AudioFocusHelper(this);
        mTelephonyHelper = new TelephonyHelper(mContext);
        mTelephonyHelper.setListener(this);
    }

    protected abstract void setCompleteListener(CompleteListener listener);

    protected abstract void setVolume(float vol);

    protected abstract void setNotificationHelper(NotificationHelper notificationHelper);

    protected abstract void reset();

    protected abstract void setDataSource(Uri uri) throws IOException;

    protected abstract void prepare() throws IOException;

    protected abstract void start();

    protected abstract void pause();

    protected abstract void stop();

    protected abstract void skipToPrevious(Uri uri) throws IOException;

    protected abstract void skipToNext(Uri uri) throws IOException;

    protected abstract boolean isPlaying();

    protected abstract long getDuration();

    protected abstract long getCurrTime();

    protected abstract long seek(int pos);

    protected abstract long getBufferLen();

    protected abstract long getTotalLen();

    protected abstract int getBufferPercent();


    //region imp

    protected abstract void onPreparedLogic(MediaPlayer mp);

    protected abstract void onCompletionLogic(MediaPlayer mp);

    protected abstract void onBufferingUpdateLogic(MediaPlayer mp, int percent);

    protected abstract void onAudioFocusChangeImp(int focusChange);

    protected abstract void onCallStateChangedImp(int state, String phoneNumber);

    protected abstract void onSeekCompletedImp(MediaPlayer mp);

    //endregion

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

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        onSeekCompletedImp(mp);
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        onAudioFocusChangeImp(focusChange);
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        onCallStateChangedImp(state, phoneNumber);
    }


    public interface CompleteListener {
        void onComplete();
    }


    //region state

    public abstract int getState();
    //endregion

}
