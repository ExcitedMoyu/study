package com.smasher.media.service;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.smasher.media.core.MediaPlayerProxy;
import com.smasher.media.loader.MusicLoader;
import com.smasher.media.manager.QueueManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaService extends MediaBrowserServiceCompat implements MediaSessionCompat.OnActiveChangeListener {


    private static final String TAG = "MediaService";
    public static final String MEDIA_ID_ROOT = "MediaService_browser_root";
    private MediaSessionCompat mSession;
    private MediaSessionCallback mSessionCallback;

    private MediaPlayerProxy mPlayer;
    private MusicLoader mLoader;
    private QueueManager mQueueManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mLoader = MusicLoader.getInstance();
        mLoader.init(this);
        mLoader.requestData();

        mSession = new MediaSessionCompat(this, TAG);
        mSessionCallback = new MediaSessionCallback();

        mSession.setCallback(mSessionCallback);
        mSession.addOnActiveChangeListener(this);

        //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
        //表示MediaBrowser与MediaBrowserService连接成功
        setSessionToken(mSession.getSessionToken());

        mQueueManager = new QueueManager(mLoader, mQueueListener);
        mPlayer = new MediaPlayerProxy(mSession);
        mPlayer.initPlay(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
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
        result.sendResult(mLoader.getChildren());

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

    public class MediaSessionCallback extends MediaSessionCompat.Callback {


        public MediaSessionCallback() {
        }


        @Override
        public void onPrepare() {
            super.onPrepare();
            mSession.setActive(true);
            mQueueManager.getCurrentMusic();
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

            QueueItem currentMusic = mQueueManager.getCurrentMusic();
            if (currentMusic != null) {
                MediaMetadataCompat metadataCompat = null;
                metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                mSession.setMetadata(metadataCompat);
                if (mPlayer != null) {
                    mPlayer.onPrepare(currentMusic);
                    mPlayer.play();
                }
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
            mQueueManager.setQueueFromMusic(mediaId);
            Log.d(TAG, "onPlayFromMediaId: ");
            QueueItem currentMusic = mQueueManager.getCurrentMusic();
            if (currentMusic != null) {
                MediaMetadataCompat metadataCompat = null;
                metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                mSession.setMetadata(metadataCompat);
                if (mPlayer != null) {
                    mPlayer.onPrepare(currentMusic);
                    mPlayer.play();
                }
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
            if (mPlayer != null) {
                mPlayer.pause();
            }
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            Log.d(TAG, "onSkipToNext: ");
            mQueueManager.skipQueuePosition(+1);
            QueueItem currentMusic = mQueueManager.getCurrentMusic();
            if (currentMusic != null) {
                MediaMetadataCompat metadataCompat = null;
                metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                mSession.setMetadata(metadataCompat);
                if (mPlayer != null) {
                    mPlayer.onPrepare(currentMusic);
                    mPlayer.play();
                }
            }
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            Log.d(TAG, "onSkipToPrevious: ");
            mQueueManager.skipQueuePosition(-1);
            QueueItem currentMusic = mQueueManager.getCurrentMusic();
            if (currentMusic != null) {
                MediaMetadataCompat metadataCompat = null;
                metadataCompat = mQueueManager.convertToMediaMetadata(currentMusic);
                mSession.setMetadata(metadataCompat);
                if (mPlayer != null) {
                    mPlayer.onPrepare(currentMusic);
                    mPlayer.play();
                }
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
}
