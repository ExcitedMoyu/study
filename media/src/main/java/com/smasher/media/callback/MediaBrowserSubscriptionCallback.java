package com.smasher.media.callback;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback {


    public MediaBrowserSubscriptionCallback() {
        super();
    }


    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
        super.onChildrenLoaded(parentId, children);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children, @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
    }

    @Override
    public void onError(@NonNull String parentId) {
        super.onError(parentId);
    }

    @Override
    public void onError(@NonNull String parentId, @NonNull Bundle options) {
        super.onError(parentId, options);
    }
}
