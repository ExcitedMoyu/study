package com.smasher.music.core;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * @author matao
 * @date 2019/5/25
 */
public class MusicPlayer {


    protected MediaPlayer mMediaPlayer;


    public MusicPlayer() {
        mMediaPlayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder attrBuilder = null;
            attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            attrBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            attrBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            mMediaPlayer.setAudioAttributes(attrBuilder.build());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

    }
}
