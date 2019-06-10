package com.smasher.media.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.smasher.media.R;
import com.smasher.media.constant.Constant;
import com.smasher.media.helper.TestHelper;
import com.smasher.media.service.MediaService;
import com.smasher.ndk.PrimaryService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * @author matao
 */
public class InitActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = "InitActivity";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";

    @BindView(R.id.buttonAddress)
    Button buttonStart;
    @BindView(R.id.buttonPermission)
    Button buttonStop;
    @BindView(R.id.buttonSkip)
    Button front;
    @BindView(R.id.tvAddress)
    TextView textView;
    @BindView(R.id.exit)
    Button exit;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private String[] permissions;
    private Handler mHandler;
    private TestHelper mTestHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        ButterKnife.bind(this);
        mHandler = new Handler(this);
        mTestHelper = new TestHelper();
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(mToolbar);
        initPermissionNeed();
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
        Intent intent = new Intent();
        intent.setClass(this, PrimaryService.class);
        startService(intent);
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
            Intent intent = new Intent();
            intent.setClass(this, DebugActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    @OnClick({R.id.buttonAddress, R.id.buttonPermission, R.id.buttonSkip,
            R.id.tvAddress, R.id.exit})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonAddress:
                textView.setText(mTestHelper.getPathString(TAG));
                break;
            case R.id.buttonPermission:
                doCheckPermission();
                break;
            case R.id.buttonSkip:
                doSkip();
                break;
            case R.id.tvAddress:
                break;
            case R.id.exit:
                exit();
                break;
            default:
                break;
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
