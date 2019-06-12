package com.smasher.media.core;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.smasher.media.helper.NotificationHelper;
import com.smasher.media.manager.PlaybackManager;

import java.io.IOException;

/**
 * @author matao
 * @date 2019/5/25
 */
public class MusicPlayer extends CorePlayer {

    private static final String TAG = "AudioPlayer";

    private NotificationHelper mNotificationHelper;
    private PlaybackManager mPlaybackManager;
    private MediaSessionCompat mSession;

    private MediaPlayer mPlayer;

    public MusicPlayer(Context context, MediaSessionCompat session) {
        super(context, session);

        mSession = session;

        mPlaybackManager = PlaybackManager.getInstance();
        mPlaybackManager.setMediaSession(session);
        mPlaybackManager.setState(PlaybackStateCompat.STATE_NONE);

        mPlayer = new MediaPlayer();
        mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    protected void setCompleteListener(CompleteListener listener) {
        mCompleteListener = listener;
    }

    @Override
    protected void setNotificationHelper(NotificationHelper notificationHelper) {
        mNotificationHelper = notificationHelper;
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

        if (mAudioFocusHelper != null) {
            boolean result = mAudioFocusHelper.requestFocus(mContext);
            Log.d(TAG, "start: focus:" + result);
        }


        if (mPlayer != null) {
            mPlayer.start();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
            mNotificationHelper.updateNotification();
        }
    }


    @Override
    protected void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_PAUSED);
            mNotificationHelper.updateNotification();

        }
    }

    @Override
    protected void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_STOPPED);
        }

        if (mAudioFocusHelper != null) {

            boolean result = mAudioFocusHelper.abandonFocus(mContext);
            Log.d(TAG, "start: focus:" + result);
        }

    }

    @Override
    protected void skipToPrevious(Uri uri) throws IOException {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS);
            mPlayer.setDataSource(uri.toString());
            mPlayer.prepare();
        }

    }

    @Override
    protected void skipToNext(Uri uri) throws IOException {
        if (mPlayer != null) {
            mPlayer.reset();
            mPlaybackManager.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT);
            mPlayer.setDataSource(uri.toString());
            mPlayer.prepare();
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
    protected void setVolume(float vol) {
        try {
            mPlayer.setVolume(vol, vol);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPreparedLogic(MediaPlayer mp) {
        Log.d(TAG, "onPreparedLogic: ");
        Bundle bundle = new Bundle();
        int duration = mp.getDuration();
        bundle.putInt("duration", duration);

        mSession.setExtras(bundle);

        if (mAudioFocusHelper != null) {
            boolean result = mAudioFocusHelper.requestFocus(mContext);
            Log.d(TAG, "start: focus:" + result);
        }
        mPlayer.start();
        mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
        mNotificationHelper.updateNotification();
    }

    @Override
    protected void onCompletionLogic(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletionLogic: ");
        mPlaybackManager.setState(PlaybackStateCompat.STATE_NONE);
        mCompleteListener.onComplete();
    }

    @Override
    protected void onBufferingUpdateLogic(MediaPlayer mp, int percent) {
        Log.d(TAG, "onBufferingUpdateLogic: ");
    }


    @Override
    protected void onSeekCompletedImp(MediaPlayer mp) {
        Log.d(TAG, "onSeekCompletedImp: " + mp.getCurrentPosition());
    }


    @Override
    public void onAudioFocusChangeImp(int focusChange) {
        Log.d(TAG, "onAudioFocusChange: ");
        if (mPlayer == null) {
            return;
        }

        boolean isPlaying = false;
        try {
            isPlaying = mPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_LOSS");
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                loseFocus(isPlaying);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_LOSS_TRANSIENT");
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                loseFocus(isPlaying);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_LOSS_TRANSIENT_CAN_DUCK");
                //短暂性丢失焦点并作降音处理
                loseFocus(isPlaying);
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_GAIN_TRANSIENT_MAY_DUCK");
                gainFocus(isPlaying);
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_GAIN_TRANSIENT");
                gainFocus(isPlaying);
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.d(TAG, "onAudioFocusChange: AUDIO_FOCUS_GAIN");
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                gainFocus(isPlaying);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onCallStateChangedImp(int state, String phoneNumber) {
        boolean isPlaying = false;
        try {
            isPlaying = mPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                // 响铃，来电话了
                // 先判断是否需要响铃，如果需要，则pause，否则不需要pause.
                AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                int ringVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                if (ringVolume > 0) {
                    mResumeAfterCall = (isPlaying || mResumeAfterCall);
                    pause();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // 电话活跃中
                mResumeAfterCall = (isPlaying || mResumeAfterCall);
                pause();
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                // 挂断了
                if (mResumeAfterCall) {
                    start();
                    mResumeAfterCall = false;
                }
                break;
            default:
                break;
        }
    }


    private void gainFocus(boolean isPlaying) {
        try {
            if (!isPlaying && mPausedByTransientLossOfFocus) {
                mPausedByTransientLossOfFocus = false;
                mPlayer.start();
                mPlaybackManager.setState(PlaybackStateCompat.STATE_PLAYING);
                mNotificationHelper.updateNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loseFocus(boolean isPlaying) {
        try {
            if (isPlaying) {
                mPausedByTransientLossOfFocus = true;
                mPlayer.pause();
                mPlaybackManager.setState(PlaybackStateCompat.STATE_PAUSED);
                mNotificationHelper.updateNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getState() {
        return mPlaybackManager.getState().getState();
    }


}
