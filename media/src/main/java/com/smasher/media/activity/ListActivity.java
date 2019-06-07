package com.smasher.media.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaControllerCompat.TransportControls;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.smasher.media.R;
import com.smasher.media.adapter.MusicListAdapter;
import com.smasher.media.adapter.OnItemClickListener;
import com.smasher.media.constant.Constant;
import com.smasher.media.helper.MediaBrowserHelper;
import com.smasher.media.service.MediaService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author moyu
 */
public class ListActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = "ListActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    @BindView(R.id.prepare)
    Button mPrepare;
    @BindView(R.id.load)
    Button mLoad;
    @BindView(R.id.stop)
    Button mStop;
    @BindView(R.id.release)
    Button mRelease;

    @BindView(R.id.previous)
    ImageButton previous;
    @BindView(R.id.play_pause)
    ImageButton playPause;
    @BindView(R.id.next)
    ImageButton next;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MediaBrowserHelper mMediaBrowserHelper;
    private MediaControllerCompat mController;
    private MusicListAdapter mAdapter;
    private MediaBrowserCompat mMediaBrowser;


    private List<MediaSessionCompat.QueueItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        initView();
        initMediaBrowser();

    }

    private void initMediaBrowser() {
        mMediaBrowserHelper = new MediaBrowserHelper(this) {
            @Override
            public void connectToSession(MediaSessionCompat.Token token) {
                Log.d(TAG, "connectToSession: ");
                mMediaBrowserHelper.isConnected();
                mMediaBrowserHelper.getRoot();
                connectToSessionImp(token);
            }
        };
        mMediaBrowser = mMediaBrowserHelper.getMediaBrowser();
        mSubscriptionCallback = new MediaBrowserSubscriptionCallback();
    }

    private void initView() {
        mAdapter = new MusicListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        updateViewState();


    }


    private void updateViewState() {
        int state = mController == null ? PlaybackStateCompat.STATE_NONE : mController.getPlaybackState().getState();
        boolean isPlaying = state == PlaybackStateCompat.STATE_PLAYING;
        playPause.setImageResource(isPlaying ? R.drawable.music_pause : R.drawable.music_play);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mMediaBrowserHelper.connect();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        mMediaBrowserHelper.disconnect();
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void connectToSessionImp(MediaSessionCompat.Token token) {

        try {
            mController = new MediaControllerCompat(this, token);
            MediaControllerCompat.setMediaController(this, mController);
            mController.registerCallback(new MediaControllerCallback());

            List<MediaSessionCompat.QueueItem> list = mController.getQueue();
            if (list == null) {
                if (mMediaBrowserHelper != null && mMediaBrowserHelper.isConnected()) {
                    subscribe("default");
                }

                //启动前台
                Intent intent = new Intent();
                intent.setAction(Constant.ACTION_FOREGROUND);
                intent.setClass(this, MediaService.class);
                startService(intent);
            } else {
                mList = list;
                mAdapter.setData(mList);
                mAdapter.notifyDataSetChanged();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.load, R.id.prepare, R.id.stop, R.id.release,
            R.id.previous, R.id.play_pause, R.id.next})
    public void onOperation(View view) {
        TransportControls transportControls = mController.getTransportControls();
        switch (view.getId()) {
            case R.id.load:


                break;
            case R.id.prepare:
                if (transportControls != null) {
                    transportControls.prepare();
                }
                break;

            case R.id.play_pause:
                if (transportControls == null) {
                    return;
                }

                int state = mController.getPlaybackState().getState();
                switch (state) {
                    case PlaybackStateCompat.STATE_PLAYING:
                        transportControls.pause();
                        break;
                    case PlaybackStateCompat.STATE_PAUSED:
                        transportControls.play();
                        break;
                    default:
                        transportControls.play();
                        break;
                }

                break;
            case R.id.next:
                if (transportControls != null) {
                    transportControls.skipToNext();
                }
                break;
            case R.id.previous:
                if (transportControls != null) {
                    transportControls.skipToPrevious();
                }
                break;
            case R.id.stop:
                break;
            case R.id.release:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .create();
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View view, int position) {
        MediaSessionCompat.QueueItem item = mList.get(position);
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.playFromMediaId(item.getDescription().getMediaId(), null);
        }
    }


    //region Subscription

    /**
     * MediaBrowserSubscriptionCallback
     */
    public class MediaBrowserSubscriptionCallback extends SubscriptionCallback {

        public static final String TAG = "SubscriptionCallback";

        MediaBrowserSubscriptionCallback() {
            super();
        }


        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaItem> children) {
            super.onChildrenLoaded(parentId, children);
            //加载到的
            Log.d(TAG, "onChildrenLoaded: " + parentId);

        }

        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaItem> children, @NonNull Bundle options) {
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

    MediaBrowserSubscriptionCallback mSubscriptionCallback;

    private void unsubscribe(String parentId) {
        mMediaBrowser.unsubscribe(parentId);
    }

    private void subscribe(String parentId) {
        unsubscribe(parentId);
        mMediaBrowser.subscribe(parentId, mSubscriptionCallback);
    }

    //endregion


    //region ControllerCallback

    /**
     * MediaControllerCallback
     */
    class MediaControllerCallback extends MediaControllerCompat.Callback {
        public static final String TAG = "ControllerCallback";

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
            String value = state != null ? String.valueOf(state.getState()) : "";
            Log.d(TAG, "onPlaybackStateChanged " + value);
            updateViewState();
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);

            String title = "";
            if (metadata != null) {
                Log.d(TAG, "onMetadataChanged: " + metadata.getDescription().getTitle());
                title = metadata.getDescription().getTitle().toString();
            } else {
                Log.d(TAG, "onMetadataChanged ");
            }
            Toast.makeText(ListActivity.this, title, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onQueueChanged(@Nullable List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
            if (queue != null) {
                Log.d(TAG, "onQueueChanged: " + queue.size());


                mList = queue;
                mAdapter.setData(mList);
                mAdapter.notifyDataSetChanged();
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


    //endregion
}
