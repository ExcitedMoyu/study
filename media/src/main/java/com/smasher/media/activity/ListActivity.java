package com.smasher.media.activity;

import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.media.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    MediaSessionCompat mMediaSession;
    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO;


    public void setupMediaSession() {
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setCallback(mCallback);
        mMediaSession.setActive(true);
    }


    /**
     * 更新播放状态，播放/暂停/拖动进度条时调用
     */
    public void updatePlaybackState() {
//        int state = isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
//        PlaybackStateCompat.Builder builder = new PlaybackStateCompat.Builder();
//        builder.setActions(MEDIA_SESSION_ACTIONS)
//                .setState(state, getCurrentPosition(), 1);
//        mMediaSession.setPlaybackState(
//                new PlaybackStateCompat.Builder()
//                        .setActions(MEDIA_SESSION_ACTIONS)
//                        .setState(state, getCurrentPosition(), 1)
//                        .build());
    }



    /**
     * 更新正在播放的音乐信息，切换歌曲时调用
     */
    public void updateMetaData(String path) {
//        if (!StringUtils.isReal(path)) {
//            mMediaSession.setMetadata(null);
//            return;
//        }
//
//        SongInfo info = mediaManager.getSongInfo(context, path);
//        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, info.getTitle())
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, info.getArtist())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, info.getAlbum())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, info.getArtist())
//                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, info.getDuration())
//                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, getCoverBitmap(info));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getCount());
//        }

//        mMediaSession.setMetadata(metaData.build());

    }


    MediaSessionCompat.Callback mCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
            super.onCommand(command, extras, cb);
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            return super.onMediaButtonEvent(mediaButtonEvent);
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
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onPlayFromSearch(String query, Bundle extras) {
            super.onPlayFromSearch(query, extras);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
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
        public void onSetRating(RatingCompat rating) {
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
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
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

        @Override
        public void onRemoveQueueItemAt(int index) {
            super.onRemoveQueueItemAt(index);
        }
    };
}
