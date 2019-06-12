package com.smasher.media.service;


import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.support.v4.media.session.MediaSessionCompat.OnActiveChangeListener;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.smasher.media.annotation.ActionType;
import com.smasher.media.constant.Constant;
import com.smasher.media.core.CorePlayer;
import com.smasher.media.core.MediaPlayerProxy;
import com.smasher.media.loader.MusicLoader;
import com.smasher.media.helper.NotificationHelper;
import com.smasher.media.manager.QueueManager;
import com.smasher.media.receiver.MediaButtonIntentReceiver;

import java.io.IOException;
import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaService extends MediaBrowserServiceCompat implements
        OnActiveChangeListener,
        CorePlayer.CompleteListener {


    private static final String TAG = "MediaService";

    private MediaSessionCompat mSession;
    private MediaSessionCallback mSessionCallback;

    private MediaPlayerProxy mPlayer;
    private MusicLoader mLoader;
    private QueueManager mQueueManager;
    private NotificationHelper mNotificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mLoader = MusicLoader.getInstance();
        mLoader.init(this);

        PendingIntent pendingIntent = null;
        ComponentName componentName = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());
        mSession = new MediaSessionCompat(this, TAG, componentName, pendingIntent);
        mSessionCallback = new MediaSessionCallback();

        mSession.setCallback(mSessionCallback);
        mSession.addOnActiveChangeListener(this);
        mSession.setActive(true);
        mSession.setPlaybackToLocal(AudioManager.STREAM_MUSIC);

        //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
        //表示MediaBrowser与MediaBrowserService连接成功
        setSessionToken(mSession.getSessionToken());

        mNotificationHelper = new NotificationHelper(this, mSession);
        mQueueManager = new QueueManager(mLoader, mQueueListener);

        mPlayer = new MediaPlayerProxy(this, mSession);
        mPlayer.setNotificationHelper(mNotificationHelper);
        mPlayer.setCompleteListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        MediaControllerCompat.TransportControls transportControls = null;
        if (mSession != null) {
            MediaControllerCompat controllerCompat = mSession.getController();
            transportControls = controllerCompat.getTransportControls();
        }
        handleAction(intent, transportControls);
        return START_STICKY;
    }

    /**
     * notification控制
     *
     * @param intent            intent
     * @param transportControls transportControls
     */
    private void handleAction(Intent intent, MediaControllerCompat.TransportControls transportControls) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {

                case Constant.ACTION_FOREGROUND:
                    startForeground(Constant.NOTIFICATION_ID, mNotificationHelper.createNotification());
                    break;

                case Constant.ACTION_NEXT:
                    if (transportControls != null) {
                        transportControls.skipToNext();
                    }
                    break;
                case Constant.ACTION_PREVIOUS:
                    if (transportControls != null) {
                        transportControls.skipToPrevious();
                    }
                    break;
                case Constant.ACTION_PAUSE:
                    if (transportControls != null) {
                        transportControls.pause();
                    }
                    break;
                case Constant.ACTION_PLAY:
                    if (transportControls != null) {
                        transportControls.play();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onActiveChanged() {
        Log.d(TAG, "onActiveChanged: " + mSession.isActive());

    }


    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        Log.d(TAG, "onGetRoot: ");
        return new BrowserRoot(Constant.MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaItem>> result) {
        Log.d(TAG, "onLoadChildren: ");
        List<MediaItem> list = mLoader.getChildren();
        if (list == null) {
            result.sendResult(null);
        } else {
            List<QueueItem> queueItemList = mQueueManager.convertToQueue(list);
            String queueTitle = "local_music";
            mSession.setQueue(queueItemList);
            mQueueManager.setCurrentQueue(queueTitle, queueItemList, "");
            result.sendResult(list);
        }
    }

    @Override
    public void onComplete() {
        QueueItem item = mQueueManager.nextMusic();
        Uri uri = getMusicUri(item);
        if (uri != null) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(uri);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 操作回调
     * 执行操作
     */
    public class MediaSessionCallback extends MediaSessionCompat.Callback {


        @Override
        public void onPrepare() {
            super.onPrepare();
            Log.d(TAG, "onPrepare: ");
        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG, "onPrepareFromMediaId: ");
            super.onPrepareFromMediaId(mediaId, extras);
        }

        @Override
        public void onPrepareFromSearch(String query, Bundle extras) {
            Log.d(TAG, "onPrepareFromSearch: ");
            super.onPrepareFromSearch(query, extras);
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            Log.d(TAG, "onPrepareFromUri: ");
            super.onPrepareFromUri(uri, extras);
        }

        @Override
        public void onPlay() {
            super.onPlay();

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            Log.d(TAG, "onPlay: ");
            int state = mPlayer.getState();
            switch (state) {
                case PlaybackStateCompat.STATE_PAUSED:
                    if (mPlayer != null) {
                        mPlayer.start();
                    }
                    break;
                case PlaybackStateCompat.STATE_NONE:
                    QueueItem currentMusic = mQueueManager.getCurrentMusic();
                    Uri uri = getMusicUri(currentMusic);
                    if (uri == null) {
                        return;
                    }

                    try {
                        mPlayer.reset();
                        mPlayer.setDataSource(uri);
                        mPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PlaybackStateCompat.STATE_PLAYING:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            super.onPlayFromSearch(query, extras);
            Log.d(TAG, "onPlayFromSearch: ");
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            Log.d(TAG, "onPlayFromMediaId: ");
            mQueueManager.skipQueuePositionByMediaId(mediaId);

            QueueItem currentMusic = mQueueManager.getCurrentMusic();
            Uri uri = getMusicUri(currentMusic);
            if (uri == null) {
                return;
            }

            try {
                mPlayer.reset();
                mPlayer.setDataSource(uri);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
            Log.d(TAG, "onPlayFromUri: ");
        }


        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
            Log.d(TAG, "onSkipToQueueItem: ");
        }

        @Override
        public void onPause() {
            super.onPause();

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            if (mPlayer.getState() == PlaybackStateCompat.STATE_PLAYING) {
                Log.d(TAG, "onPause: ");
                mPlayer.pause();
            }
        }


        @Override
        public void onSkipToNext() {
            super.onSkipToNext();

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            Log.d(TAG, "onSkipToNext: ");
            changeMusic(ActionType.ACTION_NEXT);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            Log.d(TAG, "onSkipToPrevious: ");

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            Log.d(TAG, "onSkipToPrevious: ");
            changeMusic(ActionType.ACTION_PREVIOUS);
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
            Log.d(TAG, "onFastForward: ");
        }

        @Override
        public void onRewind() {
            super.onRewind();
            Log.d(TAG, "onRewind: ");
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.d(TAG, "onStop: ");
            if (mPlayer != null) {
                mPlayer.stop();
            }
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            Log.d(TAG, "onSeekTo: ");
            if (mPlayer != null) {
                mPlayer.seek((int) pos);
            }

        }

        @Override
        public void onSetRating(@NonNull RatingCompat rating) {
            super.onSetRating(rating);
            Log.d(TAG, "onSetRating: ");
        }


        @Override
        public void onSetRating(RatingCompat rating, Bundle extras) {
            super.onSetRating(rating, extras);
            Log.d(TAG, "onSetRating: ");
        }


        @Override
        public void onSetCaptioningEnabled(boolean enabled) {
            super.onSetCaptioningEnabled(enabled);
            Log.d(TAG, "onSetCaptioningEnabled: ");
        }


        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            Log.d(TAG, "onSetRepeatMode: ");
            mQueueManager.setRepeatMode(repeatMode);
            mSession.setRepeatMode(repeatMode);
        }


        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            Log.d(TAG, "onSetShuffleMode: ");
            mQueueManager.setShuffleMode(shuffleMode);
            mSession.setShuffleMode(shuffleMode);
        }


        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            super.onAddQueueItem(description);
            Log.d(TAG, "onAddQueueItem: ");
        }


        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            super.onAddQueueItem(description, index);
            Log.d(TAG, "onAddQueueItem: ");
        }


        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
            Log.d(TAG, "onRemoveQueueItem: ");
        }

        @Override
        public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
            Log.d(TAG, "onMediaButtonEvent: ");
            return super.onMediaButtonEvent(mediaButtonIntent);
        }

        @Override
        public void onCustomAction(@NonNull String action, @Nullable Bundle extras) {
            super.onCustomAction(action, extras);
            Log.d(TAG, "onCustomAction: ");
        }


        @Override
        public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
            super.onCommand(command, args, cb);
            Log.d(TAG, "callback onCommand: ");
        }
    }


    private Uri getMusicUri(QueueItem currentMusic) {
        Uri uri = null;
        if (currentMusic != null) {
            MediaMetadataCompat metadataCompat = null;
            metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
            mSession.setMetadata(metadataCompat);
            uri = currentMusic.getDescription().getMediaUri();
        }
        return uri;
    }


    /**
     * 手动切歌
     * （上一曲/下一曲）
     *
     * @param action action
     */
    private void changeMusic(@ActionType int action) {
        switch (mPlayer.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_NONE:
                switch (action) {
                    case ActionType.ACTION_NEXT:
                        mQueueManager.skipQueuePosition(1);
                        break;
                    case ActionType.ACTION_PREVIOUS:
                        mQueueManager.skipQueuePosition(-1);
                        break;
                    default:
                        break;
                }

                QueueItem currentMusic = mQueueManager.getCurrentMusic();
                Uri uri = getMusicUri(currentMusic);
                if (uri == null) {
                    return;
                }

                try {
                    switch (action) {
                        case ActionType.ACTION_NEXT:
                            mPlayer.skipToNext(uri);
                            break;
                        case ActionType.ACTION_PREVIOUS:
                            mPlayer.skipToPrevious(uri);
                            break;
                        default:
                            break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    private QueueManager.QueueListener mQueueListener = new QueueManager.QueueListener() {
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "onMetadataChanged: ");
        }

        @Override
        public void onMetadataRetrieveError() {

        }

        @Override
        public void onCurrentQueueIndexUpdated(int queueIndex) {

        }

        @Override
        public void onQueueUpdated(String title, List<QueueItem> newQueue) {

        }
    };
}
