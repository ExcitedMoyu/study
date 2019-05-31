package com.smasher.media.callback;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaControllerCallback extends MediaControllerCompat.Callback {

    public MediaControllerCallback() {


    }


    @Override
    public void onSessionReady() {
        super.onSessionReady();
    }

    @Override
    public void onSessionDestroyed() {
        super.onSessionDestroyed();
    }

    @Override
    public void onSessionEvent(@NonNull String event, @Nullable Bundle extras) {
        super.onSessionEvent(event, extras);
    }

    @Override
    public void onPlaybackStateChanged(@Nullable PlaybackStateCompat state) {
        super.onPlaybackStateChanged(state);
    }

    @Override
    public void onMetadataChanged(@Nullable MediaMetadataCompat metadata) {
        super.onMetadataChanged(metadata);
    }

    @Override
    public void onQueueChanged(@Nullable List<MediaSessionCompat.QueueItem> queue) {
        super.onQueueChanged(queue);
    }

    @Override
    public void onQueueTitleChanged(@Nullable CharSequence title) {
        super.onQueueTitleChanged(title);
    }

    @Override
    public void onExtrasChanged(@Nullable Bundle extras) {
        super.onExtrasChanged(extras);
    }

    @Override
    public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo info) {
        super.onAudioInfoChanged(info);
    }


    @Override
    public void onCaptioningEnabledChanged(boolean enabled) {
        super.onCaptioningEnabledChanged(enabled);
    }


    @Override
    public void onRepeatModeChanged(int repeatMode) {
        super.onRepeatModeChanged(repeatMode);
    }


    @Override
    public void onShuffleModeChanged(int shuffleMode) {
        super.onShuffleModeChanged(shuffleMode);
    }
}
