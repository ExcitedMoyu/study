package com.smasher.music.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.music.R;
import com.smasher.music.activity.adapter.MusicListAdapter;
import com.smasher.music.adapter.OnItemClickListener;
import com.smasher.music.constant.Constant;
import com.smasher.music.entity.MediaInfo;
import com.smasher.music.entity.RequestInfo;
import com.smasher.music.helper.RequestHelper;
import com.smasher.music.loader.MusicLoader;
import com.smasher.music.service.MusicService;
import com.smasher.music.service.MusicService.MusicBinder;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 播放页面
 */
public class PlayListActivity extends AppCompatActivity implements Handler.Callback,
        OnItemClickListener {

    private static final String TAG = "PlayListActivity";
    @BindView(R.id.previous)
    ImageButton previous;
    @BindView(R.id.play_pause)
    ImageButton playAndPause;
    @BindView(R.id.next)
    ImageButton next;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private MusicService mMusicService;
    private Handler mHandler;
    private MusicLoader loader;
    private AudioManager mAudioMgr;
    private MusicListAdapter musicListAdapter;
    private MediaInfo current;

    private RequestHelper mHelper;

    private ArrayList<MediaInfo> mList = new ArrayList<>();


    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        mHandler = new Handler(this);

        // 从系统服务中获取音频管理器
        mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        loader = MusicLoader.getInstance(getContentResolver());
        mHelper = new RequestHelper();

        initToolbar();
        initState();
        initList();
        initData();
    }

    private void initToolbar() {
        toolbar.setTitle("MUSIC");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(mMenuItemClickListener);

    }

    private void initData() {
        mList = loader.getMusicList();
        musicListAdapter.setData(mList);
        musicListAdapter.notifyDataSetChanged();

        String foldPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        File file = new File(foldPath);
        if (!file.exists()) {
            Log.d(TAG, "play: exists: false");
            return;
        } else {
            Log.d(TAG, "play: exists: true");
            Log.d(TAG, "play: isDirectory" + file.isDirectory());
            String[] list = file.list();

        }

    }


    private void initList() {

        musicListAdapter = new MusicListAdapter(this);
        musicListAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(musicListAdapter);

    }


    private void initState() {
        if (mMusicService == null) {
            playAndPause.setTag(Constant.MUSIC_STATE_PLAY);
            playAndPause.setImageResource(R.drawable.music_play);
            return;
        }

        boolean isPlaying = mMusicService.isPlaying();
        playAndPause.setTag(isPlaying ? Constant.MUSIC_STATE_PAUSE : Constant.MUSIC_STATE_PLAY);
        playAndPause.setImageResource(isPlaying ? R.drawable.music_pause : R.drawable.music_play);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setCommandType(RequestInfo.COMMAND_START);

        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        intent.putExtra(RequestInfo.REQUEST_TAG, requestInfo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        mHandler.postDelayed(() -> {
            Intent intent1 = new Intent();
            intent1.setClass(PlayListActivity.this, MusicService.class);
            isBind = bindService(intent1, mConnection, BIND_AUTO_CREATE);
        }, 200);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBind) {
            unbindService(mConnection);
            isBind = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view, int position) {
        MediaInfo item = mList.get(position);
        String url = item.getUrl();

        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setCommandType(RequestInfo.COMMAND_PLAY);
        intent.putExtra(RequestInfo.REQUEST_TAG, requestInfo);
        intent.putExtra(RequestInfo.REQUEST_MEDIA, item);
        startService(intent);

        mHandler.postDelayed(this::initState, 500);
        Toast.makeText(this, item.getTitle() + " path:" + url, Toast.LENGTH_SHORT).show();
    }


    OnMenuItemClickListener mMenuItemClickListener = item -> {

        switch (item.getItemId()) {
            case R.id.action_exit:

                if (mMusicService != null) {
                    mMusicService.stopForeground(true);
                    mMusicService.stopSelf();
                }

                break;
            default:
                break;
        }
        return true;
    };


    private void changeMusicAction(View view, int id) {
        try {
            MediaInfo target;
            int state;
            int position = mList.indexOf(current);
            Intent intent = new Intent();
            intent.setClass(this, MusicService.class);
            RequestInfo requestInfo = new RequestInfo();
            switch (id) {
                case R.id.previous:
                    state = Constant.MUSIC_STATE_PLAY;
                    mHelper.packState(requestInfo, state);
                    target = getTargetItem(position - 1);
                    intent.putExtra(RequestInfo.REQUEST_MEDIA, target);
                    break;
                case R.id.play_pause:
                    state = (int) view.getTag();
                    mHelper.packState(requestInfo, state);
                    break;
                case R.id.next:
                    requestInfo.setCommandType(RequestInfo.COMMAND_PLAY);
                    target = getTargetItem(position + 1);
                    intent.putExtra(RequestInfo.REQUEST_MEDIA, target);
                    break;
                default:
                    break;
            }
            intent.putExtra(RequestInfo.REQUEST_TAG, requestInfo);
            startService(intent);

            mHandler.postDelayed(this::initState, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaInfo getTargetItem(int position) {
        if (position < mList.size() && position >= 0) {
            return mList.get(position);
        }
        return null;
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            MusicBinder binder = (MusicBinder) service;
            mMusicService = binder.getService();
            initState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mMusicService = null;
        }
    };


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @OnClick({R.id.previous, R.id.play_pause, R.id.next})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
            case R.id.play_pause:
            case R.id.next:
                changeMusicAction(view, id);
                break;
            default:
                break;
        }
    }
}
