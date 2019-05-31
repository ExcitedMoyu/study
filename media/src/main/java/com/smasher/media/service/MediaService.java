package com.smasher.media.service;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.smasher.media.callback.MediaSessionCallback;
import com.smasher.media.core.MediaPlayerProxy;

import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaService extends MediaBrowserServiceCompat {


    private static final String TAG = "MediaService";
    private MediaSessionCompat mSessionCompat;
    private MediaSessionCallback mSessionCallback;
    private PlaybackStateCompat mStateCompat;
    private MediaPlayerProxy mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer=new MediaPlayerProxy();
        mSessionCompat = new MediaSessionCompat(this, TAG);

        mSessionCallback = new MediaSessionCallback(mPlayer,mSessionCompat);
        mStateCompat = mSessionCallback.getStateCompat();

        mSessionCompat.setCallback(mSessionCallback);
        mSessionCompat.setPlaybackState(mStateCompat);
        //设置token后会触发MediaBrowserCompat.ConnectionCallback的回调方法
        //表示MediaBrowser与MediaBrowserService连接成功
        setSessionToken(mSessionCompat.getSessionToken());
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
