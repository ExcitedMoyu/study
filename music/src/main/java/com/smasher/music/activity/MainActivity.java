package com.smasher.music.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.music.R;
import com.smasher.music.entity.RequestInfo;
import com.smasher.music.service.MusicService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.buttonStart)
    Button buttonStart;
    @BindView(R.id.buttonPause)
    Button buttonStop;
    String[] permissions;
    public static final int REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        String state = Environment.getExternalStorageState();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setCommandType(RequestInfo.COMMAND_START);

        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        intent.putExtra(RequestInfo.REQUEST_TAG, requestInfo);
        startService(intent);

        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


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
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
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
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        stopService(intent);
        super.onDestroy();
    }


    @OnClick({R.id.buttonStart, R.id.buttonPause})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);

        RequestInfo requestInfo = new RequestInfo();

        switch (view.getId()) {
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
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
