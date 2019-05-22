package com.smasher.music.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.music.R;
import com.smasher.music.entity.RequestInfo;
import com.smasher.music.service.MusicService;
import com.smasher.music.service.MusicService.MusicBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * @author matao
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.buttonStart)
    Button buttonStart;
    @BindView(R.id.buttonPause)
    Button buttonStop;
    @BindView(R.id.front)
    Button front;


    String[] permissions;
    private Handler mHandler;
    public static final int REQUEST_CODE = 1000;

    private MusicService mMusicService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            mHandler = new Handler();
            String data = Environment.getDataDirectory().getPath();
            Log.d(TAG, "data: " + data);
            String data_abs = Environment.getDataDirectory().getAbsolutePath();
            Log.d(TAG, "data_abs: " + data_abs);

            String cache = Environment.getDownloadCacheDirectory().getPath();
            Log.d(TAG, "cache: " + cache);
            String cache_abs = Environment.getDownloadCacheDirectory().getAbsolutePath();
            Log.d(TAG, "cache_abs: " + cache_abs);

            String system = Environment.getRootDirectory().getPath();
            Log.d(TAG, "system: " + system);
            String system_abs = Environment.getRootDirectory().getAbsolutePath();
            Log.d(TAG, "system_abs: " + system_abs);

            String music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
            Log.d(TAG, "music: " + music);
            String music_abs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
            Log.d(TAG, "music_abs: " + music_abs);

//            String state = Environment.getExternalStorageState();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE};
            } else {
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @AfterPermissionGranted(REQUEST_CODE)
    private boolean checkPermissions() {
        boolean perms = EasyPermissions.hasPermissions(this, permissions);
        if (perms) {
            //do nothing
            return true;
        } else {
            PermissionRequest.Builder builder = new PermissionRequest.Builder(this, REQUEST_CODE, permissions);
            PermissionRequest request = builder.build();
            EasyPermissions.requestPermissions(request);
        }
        return false;
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

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, MusicService.class);
                bindService(intent1, mConnection, BIND_AUTO_CREATE);
            }
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
        unbindService(mConnection);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.buttonStart, R.id.buttonPause, R.id.front})
    public void onViewClicked(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonStart:
            case R.id.buttonPause:
                changeMusicAction(id);
                break;

            case R.id.front:
                if (mMusicService != null) {
                    mMusicService.startServiceFront();
                }
                break;
            default:
                break;
        }


    }

    private void changeMusicAction(int id) {
        try {
            Intent intent = new Intent();
            intent.setClass(this, MusicService.class);
            RequestInfo requestInfo = new RequestInfo();
            switch (id) {
                case R.id.buttonStart:
                    requestInfo.setCommandType(RequestInfo.COMMAND_PLAY);
                    break;
                case R.id.buttonPause:
                    requestInfo.setCommandType(RequestInfo.COMMAND_PAUSE);
                    break;
                default:
                    break;
            }
            intent.putExtra(RequestInfo.REQUEST_TAG, requestInfo);

            if (checkPermissions()) {
                startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            MusicBinder binder = (MusicBinder) service;
            mMusicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            mMusicService = null;
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
