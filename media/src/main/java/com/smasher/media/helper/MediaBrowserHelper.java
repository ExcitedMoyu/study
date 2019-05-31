package com.smasher.media.helper;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import com.smasher.media.callback.MediaBrowserConnectionCallback;
import com.smasher.media.callback.MediaBrowserSubscriptionCallback;
import com.smasher.media.service.MediaService;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaBrowserHelper {

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

    }


    public void connect() {
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.connect();
        }
    }


    public void disconnext() {
        if (mMediaBrowserCompat != null) {
            mMediaBrowserCompat.disconnect();
        }
    }


    public boolean isconnected() {
        if (mMediaBrowserCompat != null) {
            return mMediaBrowserCompat.isConnected();
        }
        return false;
    }


    public String getRoot() {
        if (mMediaBrowserCompat != null) {
            return mMediaBrowserCompat.getRoot();
        }
        return null;
    }


    public MediaBrowserCompat getMediaBrowserCompat() {
        return mMediaBrowserCompat;
    }
}
