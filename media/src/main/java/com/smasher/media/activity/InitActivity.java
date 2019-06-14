package com.smasher.media.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.smasher.media.R;
import com.smasher.media.constant.Constant;
import com.smasher.media.helper.TestHelper;
import com.smasher.media.service.MediaService;
import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.base.BaseActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * @author matao
 */
public class InitActivity extends BaseActivity implements Handler.Callback,
        View.OnClickListener {

    private static final String TAG = "InitActivity";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";

    Button buttonStart;
    Button buttonStop;
    Button front;
    TextView textView;
    Button exit;
    Toolbar mToolbar;

    private String[] permissions;
    private Handler mHandler;
    private TestHelper mTestHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 30);
        initView();
        initListener();
        initPermissionNeed();
    }

    @Override
    public void setFunctionsForFragment(String tag) {

    }

    @Override
    public View getRootView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_media_init, null);
    }

    private void initListener() {
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        front.setOnClickListener(this);
        textView.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void initView() {
        buttonStart = findViewById(R.id.buttonAddress);
        buttonStop = findViewById(R.id.buttonPermission);
        front = findViewById(R.id.buttonSkip);
        textView = findViewById(R.id.tvAddress);
        exit = findViewById(R.id.exit);
        mToolbar = findViewById(R.id.toolbar);

        mHandler = new Handler(this);
        mTestHelper = new TestHelper();
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        mToolbar.setTitle(R.string.activity_description_media_init);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void initData() {

    }

    private void initPermissionNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonAddress) {
            textView.setText(mTestHelper.getPathString(TAG));
        } else if (id == R.id.buttonPermission) {
            doCheckPermission();
        } else if (id == R.id.buttonSkip) {
            doSkip();
        } else if (id == R.id.tvAddress) {
            Log.d(TAG, "onClick: address");
        } else if (id == R.id.exit) {
            exit();
        }

    }


    private void exit() {
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_PAUSE);
        intent.setClass(this, MediaService.class);
        stopService(intent);
    }


    private void doSkip() {
        Intent intent = new Intent();
        intent.setClass(this, ListActivity.class);
        startActivity(intent);
    }

    private void doCheckPermission() {
        if (checkPermissions()) {
            Toast.makeText(this, "has permission", Toast.LENGTH_SHORT).show();
        } else {
            //do nothing
            Log.d(TAG, "permission is requesting");
        }
    }


    @AfterPermissionGranted(Constant.REQUEST_CODE)
    private boolean checkPermissions() {
        boolean perms = EasyPermissions.hasPermissions(this, permissions);
        if (perms) {
            //do nothing
            return true;
        } else {
            PermissionRequest.Builder builder = new PermissionRequest.Builder(this, Constant.REQUEST_CODE, permissions);
            PermissionRequest request = builder.build();
            EasyPermissions.requestPermissions(request);
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


}
