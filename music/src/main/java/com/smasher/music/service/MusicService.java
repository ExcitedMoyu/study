package com.smasher.music.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.smasher.music.R;
import com.smasher.music.activity.MainActivity;
import com.smasher.music.entity.RequestInfo;

import java.io.File;
import java.io.IOException;

/**
 * @author moyu
 */
public class MusicService extends Service implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    private static final String TAG = "MusicService";

    String channelId = "musicChannelId";
    String channelName = "musicChannelName";
    NotificationChannel mNotificationChannel;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    MediaPlayer mMediaPlayer;

    private Handler mHandler;

    private boolean prepared;

    private static final int NOTIFY_ID = 100000;

    public MusicService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // 声明一个处理器对象
        try {
            mHandler = new Handler();

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel = new NotificationChannel(channelId, channelName,
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(mNotificationChannel);

            }

            initIfNecessary();

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
            builder = new NotificationCompat.Builder(this, channelId);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("123")
                    .setWhen(System.currentTimeMillis())
                    .setContentText("测试Text")
                    .setContentTitle("Title")
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent);
//            startForeground(NOTIFY_ID, builder.build());

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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


        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
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


    class MusicBinder extends Binder {

        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }

    }
}
