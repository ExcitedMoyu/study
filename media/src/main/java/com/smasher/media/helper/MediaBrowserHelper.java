package com.smasher.media.helper;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smasher.media.service.MediaService;

import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public abstract class MediaBrowserHelper {

    private static final String TAG = "MediaBrowserHelper";
    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaBrowserConnectionCallback mConnectionCallback;
    private MediaBrowserSubscriptionCallback mSubscriptionCallback;


    public MediaBrowserHelper(Context context) {
        mConnectionCallback = new MediaBrowserConnectionCallback();
        mSubscriptionCallback = new MediaBrowserSubscriptionCallback();
        mMediaBrowserCompat = new MediaBrowserCompat(
                context,
                new ComponentName(context, MediaService.class),
                mConnectionCallback,
                null
        );

        mMediaBrowserCompat.getItem("default", new MediaBrowserCompat.ItemCallback() {
            @Override
            public void onItemLoaded(MediaBrowserCompat.MediaItem item) {
                super.onItemLoaded(item);
                Log.d(TAG, "onItemLoaded: ");
            }

            @Override
            public void onError(@NonNull String itemId) {
                super.onError(itemId);
                Log.d(TAG, "onItemLoaded: ");
            }
        });

    }


    public void connect() {
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.connect();
        }
    }


    public void disconnect() {
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.disconnect();
        }
    }


    public boolean isConnected() {
        if (mMediaBrowserCompat != null) {
            Log.d(TAG, "isConnected: " + mMediaBrowserCompat.isConnected());
            return mMediaBrowserCompat.isConnected();
        }
        return false;
    }


    public String getRoot() {
        if (mMediaBrowserCompat != null) {
            Log.d(TAG, "getRoot: " + mMediaBrowserCompat.getRoot());
            return mMediaBrowserCompat.getRoot();
        }
        return null;
    }


    public void unsubscribe(String parentId) {
        mMediaBrowserCompat.unsubscribe(parentId);
    }

    public void subscribe(String parentId) {
        unsubscribe(parentId);
        mMediaBrowserCompat.subscribe(parentId, mSubscriptionCallback);
    }

    public abstract void connectToSession(Token token);


    public MediaBrowserCompat getMediaBrowserCompat() {
        return mMediaBrowserCompat;
    }

    public MediaControllerCallback buildControlCallback() {
        return new MediaControllerCallback();
    }


    public class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {


        MediaBrowserSubscriptionCallback() {
            super();
        }


        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
            Log.d(TAG, "onChildrenLoaded: " + parentId);
        }

        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
            super.onChildrenLoaded(parentId, children, options);
            Log.d(TAG, "onChildrenLoaded: ");
        }

        @Override
        public void onError(@NonNull String parentId) {
            super.onError(parentId);
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onError(@NonNull String parentId, @NonNull Bundle options) {
            super.onError(parentId, options);
            Log.d(TAG, "onError: ");
        }
    }


    class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback {

        MediaBrowserConnectionCallback() {
            super();
        }

        @Override
        public void onConnected() {
            super.onConnected();
            Token token = mMediaBrowserCompat.getSessionToken();
            Log.d(TAG, "onConnected: " + token);
            connectToSession(token);
        }


        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
            Log.d(TAG, "onConnectionSuspended: ");
        }


        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.d(TAG, "onConnectionFailed: ");
        }

    }


    class MediaControllerCallback extends MediaControllerCompat.Callback {

        public MediaControllerCallback() {
            super();
        }

        @Override
        public void onSessionReady() {
            super.onSessionReady();
            Log.d(TAG, "onSessionReady: ");
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            Log.d(TAG, "onSessionDestroyed: ");
        }

        @Override
        public void onSessionEvent(@NonNull String event, @Nullable Bundle extras) {
            super.onSessionEvent(event, extras);
            Log.d(TAG, "onSessionEvent: " + event);
        }

        @Override
        public void onPlaybackStateChanged(@Nullable PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if (state != null) {
                Log.d(TAG, "onPlaybackStateChanged: " + state.getState());
            } else {
                Log.d(TAG, "onPlaybackStateChanged ");

            }
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            if (metadata != null) {
                Log.d(TAG, "onMetadataChanged: " + metadata.getDescription().getTitle());
            } else {
                Log.d(TAG, "onMetadataChanged ");
            }
        }

        @Override
        public void onQueueChanged(@Nullable List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
            if (queue != null) {
                Log.d(TAG, "onQueueChanged: " + queue.size());
            } else {
                Log.d(TAG, "onQueueChanged ");

            }
        }

        @Override
        public void onQueueTitleChanged(@Nullable CharSequence title) {
            super.onQueueTitleChanged(title);
            Log.d(TAG, "onQueueTitleChanged: " + title);
        }

        @Override
        public void onExtrasChanged(@Nullable Bundle extras) {
            super.onExtrasChanged(extras);
            Log.d(TAG, "onExtrasChanged");
        }

        @Override
        public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
            super.onAudioInfoChanged(info);
            Log.d(TAG, "onAudioInfoChanged: ");
        }


        @Override
        public void onCaptioningEnabledChanged(boolean enabled) {
            super.onCaptioningEnabledChanged(enabled);
            Log.d(TAG, "onCaptioningEnabledChanged: " + enabled);
        }


        @Override
        public void onRepeatModeChanged(int repeatMode) {
            super.onRepeatModeChanged(repeatMode);
            Log.d(TAG, "onRepeatModeChanged: ");
        }


        @Override
        public void onShuffleModeChanged(int shuffleMode) {
            super.onShuffleModeChanged(shuffleMode);
            Log.d(TAG, "onShuffleModeChanged: ");
        }
    }
}
