package com.smasher.ndk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.widget.base.BaseActivity;


/**
 * @author matao
 */
public class NDKTestActivity extends BaseActivity {

    private static final String TAG = "";
    Toolbar toolbar;
    FloatingActionButton fab;
    TextView helloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk_test);
        initListener();
        initService();
    }

    @Override
    public int getRootViewRes() {
        return R.layout.activity_ndk_test;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        helloWorld = findViewById(R.id.hello_world);
        setSupportActionBar(toolbar);
    }


    private void initListener() {
        fab.setOnClickListener(mOnClickListener);
        helloWorld.setOnClickListener(mOnClickListener);
    }

    private void initService() {
        Intent intent = new Intent();
        intent.setClass(this, PrimaryService.class);
        startService(intent);
    }


    @Override
    public void initData() {

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


    View.OnClickListener mOnClickListener = view -> {
        int i = view.getId();
        if (i == R.id.toolbar) {
            Log.d(TAG, "toolbar: ");
        } else if (i == R.id.fab) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (i == R.id.hello_world) {
            try {
                String result = new Watcher().stringFromJNI();
                Toast.makeText(NDKTestActivity.this, result, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
