package com.smasher.music.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Binder;
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
import com.smasher.music.constant.PlayerState;
import com.smasher.music.core.MediaPlayerProxy;
import com.smasher.music.core.PlayList;
import com.smasher.music.entity.MediaInfo;
import com.smasher.music.entity.RequestInfo;
import com.smasher.music.helper.AudioFocusHelper;
import com.smasher.music.helper.MediaButtonHelper;
import com.smasher.music.listener.PlayerListener;

/**
 * @author moyu
 */
public class MusicService extends Service implements
        AudioManager.OnAudioFocusChangeListener,
        Handler.Callback,
        PlayerListener {


    private static final String TAG = "MusicService";

    private Handler mHandler;
    private MediaInfo mMediaInfo;
    private static final int NOTIFY_ID = 2;

    private AudioFocusHelper mAudioFocusHelper;
    private MediaButtonHelper mMediaButtonHelper;
    private MediaPlayerProxy mPlayer;

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constant.CHANNEL_ID);
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

            if (intent != null) {
                if (intent.hasExtra(RequestInfo.REQUEST_TAG)) {
                    requestInfo = intent.getParcelableExtra(RequestInfo.REQUEST_TAG);
                }


                if (intent.hasExtra(RequestInfo.REQUEST_MEDIA)) {
                    MediaInfo mediaInfo = intent.getParcelableExtra(RequestInfo.REQUEST_MEDIA);

                    if (mediaInfo != null) {
                        isSame = mediaInfo.equals(mMediaInfo);
                        if (!isSame) {
                            mMediaInfo = mediaInfo;
                        }
                    }
                }
            }


            if (requestInfo != null) {
                switch (requestInfo.getCommandType()) {
                    case RequestInfo.COMMAND_PLAY:
                    case RequestInfo.COMMAND_PAUSE:
                        PlayerState state = mPlayer.getPlayState();
                        if (isSame) {
                            if (state == PlayerState.PLAY_STATE_PAUSE |
                                    state == PlayerState.PLAY_STATE_PAUSING) {
                                resume();
                            } else if (state == PlayerState.PLAY_STATE_PLAY) {
                                pause();
                            } else {
                                play();
                            }
                        } else {
                            play();
                        }

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
            boolean initOk = mPlayer.initPlay(this, mMediaInfo, 0);
            if (initOk) {
                mPlayer.play();
            }
        }, 100);

    }


    public boolean isPlaying() {
        if (mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }


    public void resume() {
        if (mPlayer != null) {
            mPlayer.resume();
        }
    }


    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    public void release() {
        if (mPlayer != null) {
            mPlayer.close();
            mPlayer = null;
        }
    }


    private void initIfNecessary() {
        mPlayer = new MediaPlayerProxy(this);
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
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

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


}
