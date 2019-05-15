package com.smasher.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.smasher.music.MediaInfo;

import java.io.IOException;

/**
 * @author moyu
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    /**
     * 是否正在播放
     */
    private boolean isPlaying = true;
    /**
     * 音乐信息
     */
    private MediaInfo mMusic;

    MediaPlayer mMediaPlayer;

    private Handler mHandler;

    public MusicService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 声明一个处理器对象
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // 从意图中获取是否正在播放的字段
        isPlaying = intent.getBooleanExtra("is_play", true);
        // 从意图中获取音乐信息对象
        mMusic = intent.getParcelableExtra("music");

        mHandler.postDelayed(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                play();
            }
        }, 100);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play() {
        String mFilePath = "";
        try {
            mMediaPlayer.reset();
            //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            attrBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            attrBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            mMediaPlayer.setAudioAttributes(attrBuilder.build());
            // 设置媒体数据的文件路径
            mMediaPlayer.setDataSource(mFilePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            // 媒体播放器正在播放
            // 媒体播放器停止播放
            mMediaPlayer.stop();
        }
    }
}
