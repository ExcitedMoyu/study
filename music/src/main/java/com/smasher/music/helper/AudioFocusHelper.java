package com.smasher.music.helper;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFocusRequest.Builder;
import android.media.AudioManager;
import android.os.Handler;

/**
 * @author matao
 * @date 2019/5/25
 */
public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {

    private AudioManager mAudioManager;
    private Handler mHandler;


    public AudioFocusHelper(Context context, Handler handler) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mHandler = handler;
    }


    public boolean requestFocus(AudioAttributes attributes) {

        int focus;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Builder mFocusRequestBuilder = new Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(attributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(this, mHandler);
            focus = mAudioManager.requestAudioFocus(mFocusRequestBuilder.build());
        } else {
            focus = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        return focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }


    public boolean abandonFocus(AudioAttributes attributes) {
        int focus;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Builder mFocusRequestBuilder = new Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(attributes)
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
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //短暂性丢失焦点并作降音处理
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐
                break;
            default:
                break;
        }

    }
}
