package com.smasher.media.service;


import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
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
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

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
public class MediaService extends MediaBrowserServiceCompat implements MediaSessionCompat.OnActiveChangeListener {


    private static final String TAG = "MediaService";
    public static final String MEDIA_ID_ROOT = "MediaService_browser_root";

    public static final String ACTION_FOREGROUND = "action_foreground";

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

        //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
        //表示MediaBrowser与MediaBrowserService连接成功
        setSessionToken(mSession.getSessionToken());

        mNotificationHelper = new NotificationHelper(this, mSession);
        mQueueManager = new QueueManager(mLoader, mQueueListener);

        mPlayer = new MediaPlayerProxy(this, mSession);
        mPlayer.setNotificationManager(mNotificationHelper);

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

                case ACTION_FOREGROUND:
                    startForeground(NotificationHelper.NOTIFICATION_ID, mNotificationHelper.createNotification());
                    break;

                case NotificationHelper.ACTION_NEXT:
                    if (transportControls != null) {
                        transportControls.skipToNext();
                    }
                    break;
                case NotificationHelper.ACTION_PREVIOUS:
                    if (transportControls != null) {
                        transportControls.skipToPrevious();
                    }
                    break;
                case NotificationHelper.ACTION_PAUSE:
                    if (transportControls != null) {
                        transportControls.pause();
                    }
                    break;
                case NotificationHelper.ACTION_PLAY:
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
        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaItem>> result) {
        Log.d(TAG, "onLoadChildren: ");
        List<MediaItem> list = mLoader.getChildren();
        List<QueueItem> queueItemList = mQueueManager.convertToQueue(list);
        mSession.setQueue(queueItemList);
        String queueTitle = "local_music";
        mQueueManager.setCurrentQueue(queueTitle, queueItemList, "");
        result.sendResult(list);
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
            Log.d(TAG, "onPlay: ");
            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }
            int state = mPlayer.getState();
            switch (state) {
                case PlaybackStateCompat.STATE_PAUSED:
                    if (mPlayer != null) {
                        mPlayer.start();
                    }
                    break;
                case PlaybackStateCompat.STATE_NONE:

                    try {
                        QueueItem currentMusic = mQueueManager.getCurrentMusic();
                        if (currentMusic != null) {
                            MediaMetadataCompat metadataCompat = null;
                            metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                            mSession.setMetadata(metadataCompat);
                            if (mPlayer != null) {
                                mPlayer.reset();
                                mPlayer.setDataSource(currentMusic.getDescription().getMediaUri());
                                mPlayer.prepare();
                            }
                        }
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
            Log.d(TAG, "onPlayFromMediaId: ");

            mQueueManager.skipQueuePositionByMediaId(mediaId);

            try {
                QueueItem currentMusic = mQueueManager.getCurrentMusic();
                if (currentMusic != null) {
                    MediaMetadataCompat metadataCompat = null;
                    metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                    mSession.setMetadata(metadataCompat);
                    if (mPlayer != null) {
                        mPlayer.reset();
                        mPlayer.setDataSource(currentMusic.getDescription().getMediaUri());
                        mPlayer.prepare();
                    }
                }
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
            Log.d(TAG, "onPause: ");

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            if (mPlayer.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mPlayer.pause();
            }
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            Log.d(TAG, "onSkipToNext: ");

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            try {
                switch (mPlayer.getState()) {
                    case PlaybackStateCompat.STATE_PLAYING:
                    case PlaybackStateCompat.STATE_PAUSED:
                    case PlaybackStateCompat.STATE_NONE:
                        mQueueManager.skipQueuePosition(+1);
                        QueueItem currentMusic = mQueueManager.getCurrentMusic();
                        if (currentMusic != null) {
                            MediaMetadataCompat metadataCompat = null;
                            metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                            mSession.setMetadata(metadataCompat);
                            if (mPlayer != null) {
                                mPlayer.skipToNext(currentMusic.getDescription().getMediaUri());
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            Log.d(TAG, "onSkipToPrevious: ");

            if (mPlayer == null) {
                Log.e(TAG, "onPlay: player is not init yet");
                return;
            }

            try {
                switch (mPlayer.getState()) {
                    case PlaybackStateCompat.STATE_PLAYING:
                    case PlaybackStateCompat.STATE_PAUSED:
                    case PlaybackStateCompat.STATE_NONE:
                        mQueueManager.skipQueuePosition(-1);
                        QueueItem currentMusic = mQueueManager.getCurrentMusic();
                        if (currentMusic != null) {
                            MediaMetadataCompat metadataCompat = null;
                            metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                            mSession.setMetadata(metadataCompat);
                            if (mPlayer != null) {
                                mPlayer.skipToPrevious(currentMusic.getDescription().getMediaUri());
                            }
                        }
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            Log.d(TAG, "onSeekTo: ");
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
        }


        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            Log.d(TAG, "onSetShuffleMode: ");
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

        //==========================================================================================
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
            Log.d(TAG, "onCommand: ");
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
