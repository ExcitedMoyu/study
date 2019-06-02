package com.smasher.media.control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Created on 2019/5/31.
 *
 * @author moyu
 */
public class Control {

    private MediaControllerCompat mController;
    private MediaControllerCompat.TransportControls mTransportControls;

    public Control(Context context, @NonNull MediaSessionCompat session) {
        mController = new MediaControllerCompat(context, session);
        MediaControllerCompat.setMediaController((Activity) context, mController);
        mTransportControls = mController.getTransportControls();
    }

    public Control(Context context, @NonNull MediaSessionCompat.Token sessionToken) throws RemoteException {
        mController = new MediaControllerCompat(context, sessionToken);
    }

    public void play() {

    }

    public void pause() {

    }

    public void skipToNext() {

    }

    public void skipToPrevious() {

    }

    private MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onSessionReady() {
            super.onSessionReady();
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

        @Override
        public void onSessionEvent(String event, Bundle extras) {
            super.onSessionEvent(event, extras);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }

        @Override
        public void onQueueTitleChanged(CharSequence title) {
            super.onQueueTitleChanged(title);
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
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

        @Override
        public void binderDied() {
            super.binderDied();
        }
    };

}
