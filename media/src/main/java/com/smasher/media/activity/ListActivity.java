package com.smasher.media.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaControllerCompat.TransportControls;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.media.R;
import com.smasher.media.helper.MediaBrowserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author moyu
 */
public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.prepare)
    Button mPrepare;
    @BindView(R.id.play)
    Button mPlay;
    @BindView(R.id.pause)
    Button mStart;
    @BindView(R.id.next)
    Button mStop;
    @BindView(R.id.previous)
    Button mRelease;
    @BindView(R.id.load)
    Button mLoad;
    @BindView(R.id.stop)
    Button mButton6;
    @BindView(R.id.release)
    Button mButton7;
    @BindView(R.id.produce)
    Button mButton8;
    @BindView(R.id.producer)
    Button mButton9;


    private MediaBrowserHelper mMediaBrowserHelper;
    private TransportControls mController;

    private boolean isLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mMediaBrowserHelper = new MediaBrowserHelper(this) {
            @Override
            public void connectToSession(MediaSessionCompat.Token token) {
                Log.d(TAG, "connectToSession: ");
                mMediaBrowserHelper.isConnected();
                mMediaBrowserHelper.getRoot();
                connectToSessionImp(token);
            }
        };
    }

    private void connectToSessionImp(MediaSessionCompat.Token token) {

        try {
            MediaControllerCompat controller = new MediaControllerCompat(this, token);
            MediaControllerCompat.setMediaController(this, controller);
            controller.registerCallback(mMediaBrowserHelper.buildControlCallback());
            mController = controller.getTransportControls();
            mMediaBrowserHelper.getMediaBrowserCompat().getItem("default", new MediaBrowserCompat.ItemCallback() {
                @Override
                public void onItemLoaded(MediaBrowserCompat.MediaItem item) {
                    super.onItemLoaded(item);
                    isLoad = true;
                }

                @Override
                public void onError(@NonNull String itemId) {
                    super.onError(itemId);
                }
            });

        } catch (RemoteException e) {
            e.printStackTrace();
        }

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


    @OnClick({R.id.load, R.id.fab, R.id.prepare, R.id.play, R.id.pause, R.id.next, R.id.previous,
            R.id.stop, R.id.release, R.id.produce, R.id.producer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.load:
                if (mMediaBrowserHelper != null && mMediaBrowserHelper.isConnected()) {
                    mMediaBrowserHelper.subscribe("default");
                }
                break;
            case R.id.prepare:
                if (mController != null) {
                    mController.prepare();
                }
                break;
            case R.id.play:
                if (mController != null) {
                    Log.d(TAG, "play:isLoad=" + isLoad);

                    if (isLoad) {
                        mController.play();
                    } else {
                        mController.playFromMediaId("default", null);
                    }
                }
                break;
            case R.id.pause:

                if (mController != null) {
                    mController.pause();
                }
                break;
            case R.id.next:
                if (mController != null) {
                    mController.skipToNext();
                }
                break;
            case R.id.previous:
                if (mController != null) {
                    mController.skipToPrevious();
                }
                break;
            case R.id.stop:
                break;
            case R.id.release:
                break;
            case R.id.produce:
                break;
            case R.id.producer:
                break;
            default:
                break;
        }
    }

}
