package com.smasher.media.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.smasher.media.R;
import com.smasher.media.adapter.MusicListAdapter;
import com.smasher.media.annotation.PlayMode;
import com.smasher.media.constant.Constant;
import com.smasher.media.helper.MediaBrowserHelper;
import com.smasher.media.service.MediaService;
import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.base.OnItemClickListener;

import java.util.List;


/**
 * @author moyu
 */
public class ListActivity extends AppCompatActivity implements OnItemClickListener,
        View.OnClickListener {

    private static final String TAG = "ListActivity";

    Toolbar mToolbar;
    CollapsingToolbarLayout mToolbarLayout;
    AppBarLayout mAppBar;

    Button mPrepare;
    Button mLoad;
    Button mStop;
    Button mRelease;

    ImageButton previous;
    ImageButton playPause;
    ImageButton next;
    ImageButton mMode;
    ImageButton mBtnList;

    RecyclerView recyclerView;
    ConstraintLayout mControl;

    private MediaBrowserHelper mMediaBrowserHelper;
    private MediaControllerCompat mController;
    private MusicListAdapter mAdapter;
    private MediaBrowserCompat mMediaBrowser;

    private List<MediaSessionCompat.QueueItem> mList;

    private int mPlayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
        initView();
        initListener();
        initState();
        initMediaBrowser();
    }

    private void initListener() {
        mPrepare.setOnClickListener(this);
        mLoad.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mRelease.setOnClickListener(this);

        previous.setOnClickListener(this);
        playPause.setOnClickListener(this);
        next.setOnClickListener(this);
        mMode.setOnClickListener(this);
        mBtnList.setOnClickListener(this);
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
        mToolbar = findViewById(R.id.toolbar);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mAppBar = findViewById(R.id.app_bar);

        mPrepare = findViewById(R.id.prepare);
        mLoad = findViewById(R.id.load);
        mStop = findViewById(R.id.stop);
        mRelease = findViewById(R.id.release);

        previous = findViewById(R.id.previous);
        playPause = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        recyclerView = findViewById(R.id.recyclerView);
        mMode = findViewById(R.id.mode);
        mBtnList = findViewById(R.id.list);
        mControl = findViewById(R.id.control);
    }


    private void initState() {
        mAdapter = new MusicListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.space);
        if (drawable != null) {
            drawable.setAlpha(180);
        }
        mControl.setBackground(drawable);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        updatePlayState();
        updateModeState();

    }


    private void updatePlayState() {
        int state = mController == null ? PlaybackStateCompat.STATE_NONE : mController.getPlaybackState().getState();
        boolean isPlaying = state == PlaybackStateCompat.STATE_PLAYING;
        playStateChangeAnimation(playPause, isPlaying ? R.drawable.music_pause : R.drawable.music_play_small);
    }

    private void playStateChangeAnimation(ImageView target, int resource) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorOutAlpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.2f);
        animatorOutAlpha.setDuration(400);
        animatorOutAlpha.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 0.6f);
        animatorScaleY.setDuration(400);
        animatorScaleY.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 0.6f);
        animatorScaleX.setDuration(400);
        animatorScaleX.setInterpolator(new LinearInterpolator());

        ObjectAnimator animatorInScaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.6f, 1.0f);
        animatorInScaleY.setDuration(400);
        animatorInScaleY.setStartDelay(300);
        animatorInScaleY.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorInScaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.6f, 1.0f);
        animatorInScaleX.setDuration(400);
        animatorInScaleX.setStartDelay(300);
        animatorInScaleX.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorIn = ObjectAnimator.ofFloat(target, "alpha", 0.6f, 1.0f);
        animatorIn.setStartDelay(300);
        animatorIn.setDuration(400);
        animatorIn.setInterpolator(new AccelerateInterpolator());
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setImageResource(resource);
            }
        });
        set.playTogether(animatorOutAlpha, animatorScaleX, animatorScaleY, animatorInScaleX, animatorInScaleY, animatorIn);
        set.start();
    }


    private void updateModeState() {
        Log.d(TAG, "updateModeState: play mode:" + mPlayMode);
        switch (mPlayMode) {
            case PlayMode.PLAY_MODE_NONE:
                modeChangeAnimation(mMode, R.drawable.play_mode_sequential);
                break;
            case PlayMode.PLAY_MODE_SINGLE:
                modeChangeAnimation(mMode, R.drawable.play_mode_single);
                break;
            case PlayMode.PLAY_MODE_CIRCULATE:
                modeChangeAnimation(mMode, R.drawable.play_mode_circle);
                break;
            case PlayMode.PLAY_MODE_SHUFFLE:
                modeChangeAnimation(mMode, R.drawable.play_mode_random);
                break;
            default:
                break;
        }
    }


    private void modeChangeAnimation(ImageView target, int resource) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorOutAlpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.2f);
        animatorOutAlpha.setDuration(400);
        animatorOutAlpha.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorOut = ObjectAnimator.ofFloat(target, "rotation", 0.0f, 360.0f);
        animatorOut.setDuration(400);
        animatorOut.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorIn = ObjectAnimator.ofFloat(target, "alpha", 0.2f, 1.0f);
        animatorIn.setStartDelay(300);
        animatorIn.setDuration(400);
        animatorIn.setInterpolator(new AccelerateInterpolator());
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setImageResource(resource);
            }
        });
        set.playTogether(animatorOutAlpha, animatorOut, animatorIn);
        set.start();
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
                if (mController != null) {
                    String mediaId = mController.getMetadata().getDescription().getMediaId();
                    mList = list;
                    mAdapter.setData(mList);
                    mAdapter.setSelectedMediaId(mediaId);
                    mAdapter.notifyDataSetChanged();
                    updatePlayState();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //region 点击事件

    @Override
    public void onClick(View view, int position) {
        MediaSessionCompat.QueueItem item = mList.get(position);
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.playFromMediaId(item.getDescription().getMediaId(), null);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mode) {
            onModeClicked();
        } else if (id == R.id.list) {
            onListClicked();
        } else if (id == R.id.load) {
            onLoadClicked();
        } else if (id == R.id.stop) {
            onStopClicked();
        } else if (id == R.id.release) {
            onReleaseClicked();
        } else if (id == R.id.prepare) {
            onPrepareClicked();
        } else if (id == R.id.previous) {
            onPreviousClicked();
        } else if (id == R.id.play_pause) {
            onPlayPauseClicked();
        } else if (id == R.id.next) {
            onNextClicked();
        }

    }


    public void onModeClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            mPlayMode = nextPlayMode(mPlayMode);
            switch (mPlayMode) {
                case PlayMode.PLAY_MODE_NONE:
                    transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
                    break;
                case PlayMode.PLAY_MODE_SINGLE:
                    transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
                    break;
                case PlayMode.PLAY_MODE_CIRCULATE:
                    transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
                    break;
                case PlayMode.PLAY_MODE_SHUFFLE:
                    transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
                    break;
                default:
                    transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
                    transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
                    break;
            }
            updateModeState();
        }
    }

    public void onListClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            Log.d(TAG, "onListClicked: ");
        }
    }

    public void onLoadClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            Log.d(TAG, "onLoadClicked: ");
        }
    }

    public void onStopClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.stop();
        }
    }

    public void onReleaseClicked() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .create();
    }

    public void onPrepareClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.prepare();
        }
    }

    public void onPreviousClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.skipToPrevious();
        }
        playStateChangeAnimation(previous, R.drawable.music_previous);
    }

    public void onPlayPauseClicked() {
        TransportControls transportControls = mController.getTransportControls();
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
    }

    public void onNextClicked() {
        TransportControls transportControls = mController.getTransportControls();
        if (transportControls != null) {
            transportControls.skipToNext();
        }
        playStateChangeAnimation(next, R.drawable.music_next);
    }

    //endregion

    private int nextPlayMode(int playMode) {
        switch (playMode) {
            case PlayMode.PLAY_MODE_PREPARE:
                playMode = PlayMode.PLAY_MODE_NONE;
                break;
            case PlayMode.PLAY_MODE_NONE:
                playMode = PlayMode.PLAY_MODE_SINGLE;
                break;
            case PlayMode.PLAY_MODE_SINGLE:
                playMode = PlayMode.PLAY_MODE_CIRCULATE;
                break;
            case PlayMode.PLAY_MODE_CIRCULATE:
                playMode = PlayMode.PLAY_MODE_SHUFFLE;
                break;
            case PlayMode.PLAY_MODE_SHUFFLE:
                playMode = PlayMode.PLAY_MODE_NONE;
                break;
            default:
                break;
        }
        return playMode;
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
            updatePlayState();
        }

        @Override
        public void onMetadataChanged(@Nullable MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);

            String title = "";
            if (metadata != null) {
                Log.d(TAG, "onMetadataChanged: " + metadata.getDescription().getTitle());
                title = metadata.getDescription().getTitle().toString();
                mAdapter.setSelectedMediaId(metadata.getDescription().getMediaId());
                mAdapter.notifyDataSetChanged();
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
