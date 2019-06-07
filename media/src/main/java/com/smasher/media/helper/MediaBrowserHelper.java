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


    public MediaBrowserHelper(Context context) {
        mConnectionCallback = new MediaBrowserConnectionCallback();
        mMediaBrowserCompat = new MediaBrowserCompat(
                context,
                new ComponentName(context, MediaService.class),
                mConnectionCallback,
                null
        );
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


    public abstract void connectToSession(Token token);


    public MediaBrowserCompat getMediaBrowser() {
        return mMediaBrowserCompat;
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


}
