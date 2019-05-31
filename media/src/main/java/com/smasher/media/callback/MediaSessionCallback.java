package com.smasher.media.callback;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smasher.media.core.MediaPlayerProxy;

/**
 * @author matao
 * @date 2019/5/31
 */
public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private PlaybackStateCompat mStateCompat;
    private PlaybackStateCompat.Builder mBuilder;
    private MediaSessionCompat mSession;
    private MediaPlayerProxy mPlayer;

    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO;

    public MediaSessionCallback(MediaPlayerProxy mediaPlayer, MediaSessionCompat sessionCompat) {
        mPlayer = mediaPlayer;
        mSession = sessionCompat;
        mBuilder = new PlaybackStateCompat.Builder();
        mBuilder.setActions(MEDIA_SESSION_ACTIONS);
        mBuilder.setState(PlaybackStateCompat.STATE_NONE, 0, 1f);
        mStateCompat = mBuilder.build();
    }


    public PlaybackStateCompat getStateCompat() {
        return mStateCompat;
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, Bundle extras) {
        super.onPrepareFromMediaId(mediaId, extras);
    }

    @Override
    public void onPrepareFromSearch(String query, Bundle extras) {
        super.onPrepareFromSearch(query, extras);
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle extras) {
        super.onPrepareFromUri(uri, extras);
    }

    @Override
    public void onPlay() {
        super.onPlay();
        if (mStateCompat.getState() == PlaybackStateCompat.STATE_PAUSED) {
            mBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f);
            mStateCompat = mBuilder.build();
            mPlayer.play();
        }
    }

    @Override
    public void onPlayFromSearch(String query, Bundle extras) {
        super.onPlayFromSearch(query, extras);
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
        super.onPlayFromMediaId(mediaId, extras);
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle extras) {
        super.onPlayFromUri(uri, extras);
        switch (mStateCompat.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_NONE:

                mBuilder.setState(PlaybackStateCompat.STATE_CONNECTING, 0, 1.0f);
                mStateCompat = mBuilder.build();
                mSession.setPlaybackState(mStateCompat);
                //我们可以保存当前播放音乐的信息，以便客户端刷新UI
                mSession.setMetadata(new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, extras.getString("title"))
                        .build());
                mPlayer.reset();
                mPlayer.setDataSource(MusicService.this, uri);
                mPlayer.prepare();//准备同步
                break;
            default:
                break;
        }

    }


    @Override
    public void onSkipToQueueItem(long id) {
        super.onSkipToQueueItem(id);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();
    }

    @Override
    public void onSkipToPrevious() {
        super.onSkipToPrevious();
    }

    @Override
    public void onFastForward() {
        super.onFastForward();
    }

    @Override
    public void onRewind() {
        super.onRewind();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
    }

    @Override
    public void onSetRating(@NonNull RatingCompat rating) {
        super.onSetRating(rating);
    }


    @Override
    public void onSetRating(RatingCompat rating, Bundle extras) {
        super.onSetRating(rating, extras);
    }


    @Override
    public void onSetCaptioningEnabled(boolean enabled) {
        super.onSetCaptioningEnabled(enabled);
    }


    @Override
    public void onSetRepeatMode(int repeatMode) {
        super.onSetRepeatMode(repeatMode);
    }


    @Override
    public void onSetShuffleMode(int shuffleMode) {
        super.onSetShuffleMode(shuffleMode);
    }


    @Override
    public void onAddQueueItem(MediaDescriptionCompat description) {
        super.onAddQueueItem(description);
    }


    @Override
    public void onAddQueueItem(MediaDescriptionCompat description, int index) {
        super.onAddQueueItem(description, index);
    }


    @Override
    public void onRemoveQueueItem(MediaDescriptionCompat description) {
        super.onRemoveQueueItem(description);
    }

    //==========================================================================================
    @Override
    public boolean onMediaButtonEvent(@NonNull Intent mediaButtonIntent) {
        return super.onMediaButtonEvent(mediaButtonIntent);
    }


    @Override
    public void onCustomAction(@NonNull String action, @Nullable Bundle extras) {
        super.onCustomAction(action, extras);
    }


    @Override
    public void onCommand(@NonNull String command, @Nullable Bundle args, @Nullable ResultReceiver cb) {
        super.onCommand(command, args, cb);
    }


}
