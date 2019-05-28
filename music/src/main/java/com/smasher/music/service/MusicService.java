package com.smasher.music.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.smasher.music.R;
import com.smasher.music.constant.Constant;
import com.smasher.music.core.MusicPlayer;
import com.smasher.music.core.PlayList;
import com.smasher.music.core.PlayerListener;
import com.smasher.music.entity.MediaInfo;
import com.smasher.music.entity.RequestInfo;
import com.smasher.music.helper.AudioFocusHelper;
import com.smasher.music.helper.MediaButtonHelper;
import com.smasher.music.listener.PlayListener;

import java.io.IOException;

/**
 * @author moyu
 */
public class MusicService extends Service implements
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        AudioManager.OnAudioFocusChangeListener,
        Handler.Callback,
        PlayerListener {


    private static final String TAG = "MusicService";

    private Handler mHandler;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private AudioAttributes mAudioAttributes;
    private AudioFocusRequest mFocusRequest;
    private MediaInfo mMediaInfo;
    private boolean prepared;
    private static final int NOTIFY_ID = 2;


    private AudioFocusHelper mAudioFocusHelper;
    private MediaButtonHelper mMediaButtonHelper;
    private MusicPlayer mMusicPlayer;
    private PlayListener mPlayListener;
    private int mPlayMode = -1;
    private PlayList mPlayList = null;


    public MusicService() {
        mPlayList = new PlayList();
        mHandler = new Handler(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");


        // 注册监听手机状态
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(mPsir, PhoneStateListener.LISTEN_CALL_STATE);


        // 声明一个处理器对象
        try {
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


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music_play);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constant.channelId);
        builder.setTicker("music")
                .setContentText("服务运行中...")
                .setContentTitle("播放器前台服务")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(bitmap);

        return builder.build();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        try {

            boolean isSame = true;

            RequestInfo requestInfo = null;
            if (intent.hasExtra(RequestInfo.REQUEST_TAG)) {
                requestInfo = intent.getParcelableExtra(RequestInfo.REQUEST_TAG);
            }


            if (intent.hasExtra(RequestInfo.REQUEST_MEDIA)) {
                MediaInfo mediaInfo = intent.getParcelableExtra(RequestInfo.REQUEST_MEDIA);

                if (mediaInfo != null) {
                    isSame = mediaInfo.equals(mMediaInfo);
                    if (!isSame) {
                        mMediaInfo = mediaInfo;

                        if (mPlayListener != null) {
                            mPlayListener.onPlayItemChanged(mMediaInfo);
                        }
                    }
                }
            }


            if (requestInfo != null) {
                switch (requestInfo.getCommandType()) {
                    case RequestInfo.COMMAND_PLAY:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mAudioManager.requestAudioFocus(mFocusRequest);
                        }

                        if (prepared && isSame) {
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

            if (mMediaInfo == null) {
                return;
            }
            String mFilePath = mMediaInfo.getUrl();

            if (TextUtils.isEmpty(mFilePath)) {
                return;
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

        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setVolume(1f, 1f);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
            attrBuilder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
            attrBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            mAudioAttributes = attrBuilder.build();
            mMediaPlayer.setAudioAttributes(mAudioAttributes);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioFocusRequest.Builder mFocusRequestBuilder = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN);
                mFocusRequest = mFocusRequestBuilder.setAudioAttributes(mAudioAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setWillPauseWhenDucked(true)
                        .setOnAudioFocusChangeListener(this, mHandler).build();

            } else {

            }
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }


        mMusicPlayer = new MusicPlayer(this, null);
        // 播放列表
        mPlayList.setOnNotifyChangeListener(this);

        mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), mHandler);

        mMediaButtonHelper = new MediaButtonHelper(getApplicationContext());

    }


    private void registerMediaButton() {
        if (mMediaButtonHelper != null) {
            mMediaButtonHelper.registerMediaButtonEventReceiver();
        }
    }

    private void unregisterMediaButton() {
        if (mMediaButtonHelper != null) {
            mMediaButtonHelper.unregisterMediaButtonEventReceiver();
        }
    }

    private void reuqestAudioFocus(AudioAttributes audioAttributes) {
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.requestFocus(audioAttributes);
        }
    }

    private void abandonAudioFocus(AudioAttributes audioAttributes) {
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.abandonFocus(audioAttributes);
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

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                //会触发此回调事件，例如播放短视频，拨打电话等。
                //通常需要暂停音乐播放
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //短暂性丢失焦点并作降音处理
                pause();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                //当其他应用申请焦点之后又释放焦点会触发此回调
                //可重新播放音乐

                if (prepared) {
                    start();
                } else {
                    play();
                }

                break;
            default:
                break;
        }
    }

    private PhoneStateListener mPsir = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
//            switch (state) {
//                case TelephonyManager.CALL_STATE_RINGING:
//                    // 响铃，来电话了
//                    // 先判断是否需要响铃，如果需要，则pause，否则不需要pause.
//                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                    int ringvolume = audioManager
//                            .getStreamVolume(AudioManager.STREAM_RING);
//                    if (ringvolume > 0) {
//                        mResumeAfterCall = (isPlayingOnTheSurface() || mResumeAfterCall)
//                                && (mPlayList.currentPosValid());
//                        pauseLogic(false);
//                    }
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    // 电话活跃中
//                    mResumeAfterCall = (isPlayingOnTheSurface() || mResumeAfterCall)
//                            && (mPlayList.currentPosValid());
//                    pauseLogic(false);
//                    break;
//                case TelephonyManager.CALL_STATE_IDLE:
//                    // 挂断了
//                    if (mResumeAfterCall) {
//                        resumeLogic();
//                        mResumeAfterCall = false;
//                    }
//                    break;
//                default:
//                    break;
//            }
        }
    };


    public void setPlayMode(int playMode) {
        if (mPlayMode == playMode) {
            return;
        }

        mPlayMode = playMode;
        switch (playMode) {
            case PlayList.PLAY_MODE_ONESHOT:
                mPlayList.setOneShotMode(true);
                mPlayList.setRepeatMode(false);
                mPlayList.setShuffleMode(false);
                break;
            case PlayList.PLAY_MODE_ONESHOT_REPEAT:
                mPlayList.setOneShotMode(true);
                mPlayList.setRepeatMode(true);
                mPlayList.setShuffleMode(false);
                break;
            case PlayList.PLAY_MODE_LIST:
                mPlayList.setOneShotMode(false);
                mPlayList.setRepeatMode(false);
                mPlayList.setShuffleMode(false);
                break;
            case PlayList.PLAY_MODE_LIST_REPEAT:
                mPlayList.setOneShotMode(false);
                mPlayList.setRepeatMode(true);
                mPlayList.setShuffleMode(false);
                break;
            case PlayList.PLAY_MODE_LIST_SHUFFLE:
                mPlayList.setOneShotMode(false);
                mPlayList.setRepeatMode(false);
                mPlayList.setShuffleMode(true);
                break;
            case PlayList.PLAY_MODE_LIST_SHUFFLE_REPEAT:
                mPlayList.setOneShotMode(false);
                mPlayList.setRepeatMode(true);
                mPlayList.setShuffleMode(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void notifyEvent(int what, int subWhat, Object ex) {

    }


    public class MusicBinder extends Binder {

        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }

    }


    public void startServiceFront() {
        mHandler.postDelayed(() -> startForeground(NOTIFY_ID, getNotify()), 200);
    }


    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }


    public void setPlayListener(PlayListener playListener) {
        mPlayListener = playListener;
    }


}
