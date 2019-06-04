package com.smasher.media.helper;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;


/**
 * @author matao
 * @date 2019/5/25
 */
public class AudioFocusHelper {


    private static final String TAG = "AudioFocusHelper";


    private AudioManager.OnAudioFocusChangeListener mListener;
    private AudioManager mAudioManager;
    private AudioAttributes mAudioAttributes;

    private AudioFocusRequest mAudioFocusRequest;


    public AudioFocusHelper(AudioManager.OnAudioFocusChangeListener listener) {
        mListener = listener;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder builder = new AudioAttributes.Builder();
            builder.setUsage(AudioAttributes.USAGE_MEDIA);
            builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            mAudioAttributes = builder.build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioFocusRequest.Builder mFocusRequestBuilder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(mListener)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true);
            mAudioFocusRequest = mFocusRequestBuilder.build();
        }
    }


    public boolean requestFocus(Context context) {

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (mAudioManager == null) {
            Log.e(TAG, "requestFocus: is not init yet");
            return false;
        }
        int focus;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mAudioFocusRequest != null) {
            focus = mAudioManager.requestAudioFocus(mAudioFocusRequest);
        } else {
            focus = mAudioManager.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        boolean result = focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

        Log.d(TAG, "requestFocus: result:" + result);

        return result;
    }


    public boolean abandonFocus(Context context) {

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager == null) {
            Log.e(TAG, "requestFocus: is not init yet");
            return false;
        }
        int focus;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mAudioFocusRequest != null) {
            focus = mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
        } else {
            focus = mAudioManager.abandonAudioFocus(mListener);
        }
        boolean result = focus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;

        Log.d(TAG, "requestFocus: result:" + result);

        return result;
    }


}
