package com.smasher.music.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.smasher.music.R;
import com.smasher.music.constant.Constant;
import com.smasher.music.entity.RequestInfo;

import java.io.File;
import java.io.IOException;

/**
 * @author moyu
 */
public class MusicService extends Service implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        Handler.Callback {

    private static final String TAG = "MusicService";

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private boolean prepared;
    private static final int NOTIFY_ID = 2;

    public MusicService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // 声明一个处理器对象
        try {
            mHandler = new Handler(this);

            initIfNecessary();

            startServiceFront();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Notification getNotify() {

        RemoteViews notifyMusic = new RemoteViews(getPackageName(), R.layout.notify_music);
        // 设置播放图标
        notifyMusic.setImageViewResource(R.id.iv_play, R.drawable.btn_play);
        // 设置文本文字
        notifyMusic.setTextViewText(R.id.tv_play, "暂停播放");
        // 设置已播放的时间
        notifyMusic.setTextViewText(R.id.tv_time, "00:00");
        // 设置远程视图内部的进度条属性
        notifyMusic.setProgressBar(R.id.pb_play, 100, 50, false);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.btn_play);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constant.channelId);
        builder.setTicker("music")
                .setContentText("Text")
                .setContentTitle("Title")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(bitmap);

        return builder.build();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        try {
            RequestInfo requestInfo = null;
            if ((intent.hasExtra(RequestInfo.REQUEST_TAG))) {
                requestInfo = intent.getParcelableExtra(RequestInfo.REQUEST_TAG);
            }

            if (requestInfo != null) {
                switch (requestInfo.getCommandType()) {
                    case RequestInfo.COMMAND_PLAY:
                        if (prepared) {
                            start();
                        } else {
                            play();
                        }
                        break;
                    case RequestInfo.COMMAND_PAUSE:
                        pause();
                        break;
                    case RequestInfo.COMMAND_NEXT:
                        break;
                    case RequestInfo.COMMAND_PREVIOUS:
                        break;
                    default:
                        break;
                }
            }

            flags = START_STICKY;
            return START_STICKY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        // 停止前台服务--参数：表示是否移除之前的通知

        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }


    public void play() {
        mHandler.postDelayed(() -> {
            String mFilePath = "";
            String foldPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
            File file = new File(foldPath);
            if (!file.exists()) {
                Log.d(TAG, "play: exists: false");
                return;
            } else {
                Log.d(TAG, "play: exists: true");
                Log.d(TAG, "play: isDirectory" + file.isDirectory());
                String[] list = file.list();
                for (String name : list) {
                    Log.d(TAG, "play: name:" + name);
                    mFilePath = foldPath + "/" + name;
                }

            }
            try {
                prepared = false;
                mMediaPlayer.reset();

                // 设置媒体数据的文件路径
                mMediaPlayer.setDataSource(mFilePath);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, 100);

    }


    public void start() {
        if (prepared && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }


    public void pause() {
        if (prepared && mMediaPlayer.isPlaying()) {
            // 媒体播放器正在播放
            // 媒体播放器停止播放
            mMediaPlayer.pause();
        }
    }


    public void release() {
        if (mMediaPlayer != null) {
            prepared = false;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    private void initIfNecessary() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
                attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
                attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
                attrBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
                attrBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
                mMediaPlayer.setAudioAttributes(attrBuilder.build());
            } else {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        prepared = false;
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        prepared = false;

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        prepared = true;
        start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    public class MusicBinder extends Binder {

        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }

    }


    public void startServiceFront() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startForeground(NOTIFY_ID, getNotify());
            }
        }, 200);
    }
}
