package com.smasher.music.helper;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager;
import android.os.Handler;
import android.os.RemoteException;

import com.smasher.music.IMusicService;

/**
 * @author matao
 * @date 2019/5/25
 */
public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
    private IMusicService mPlayer;
    private AudioManager mAudioManager;
    private Handler mHandler;
    private boolean mPausedByTransientLossOfFocus;

    protected AudioAttributes mAudioAttributes;
    protected AudioAttributes.Builder mBuilder;

    public AudioFocusHelper(Context context, IMusicService player) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mPlayer = player;
        mHandler = new Handler();
        mAudioAttributes = getAudioAttributes();
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }


    public AudioAttributes getAudioAttributes() {
        if (mAudioAttributes == null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mBuilder = new AudioAttributes.Builder();
                mBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
                mBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
                mBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
                mBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
                mAudioAttributes = mBuilder.build();
            }
        }
        return mAudioAttributes;
    }


    public boolean requestFocus() {

        int focus;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Builder mFocusRequestBuilder = new Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(this, mHandler);
            focus = mAudioManager.requestAudioFocus(mFocusRequestBuilder.build());
        } else {
            focus = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        return focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }


    public boolean abandonFocus() {
        int focus;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Builder mFocusRequestBuilder = new Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(this, mHandler);
            focus = mAudioManager.abandonAudioFocusRequest(mFocusRequestBuilder.build());
        } else {
            focus = mAudioManager.abandonAudioFocus(this);
        }
        return focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }


    @Override
    public void onAudioFocusChange(int focusChange) {

        if (mPlayer == null) {
            return;
        }

        boolean isPlaying = false;
        try {
            isPlaying = mPlayer.isPlayingOnTheSurface();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音

                try {
                    if (isPlaying) {
                        mPausedByTransientLossOfFocus = true;
                        mPlayer.pause(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //短暂性丢失焦点并作降音处理

                try {
                    if (isPlaying) {
                        mPausedByTransientLossOfFocus = true;
                        mPlayer.pause(false);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                try {
                    if (!isPlaying && mPausedByTransientLossOfFocus) {
                        mPausedByTransientLossOfFocus = false;
                        mPlayer.resume();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }
}
