package com.smasher.widget.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.core.activityoptions.ActivityManager;
import com.smasher.core.log.Logger;
import com.smasher.core.other.BusProvider;
import com.smasher.widget.receiver.NetworkReceiver;
import com.smasher.widget.receiver.NetworkReceiver.NetStatusMonitor;

import butterknife.ButterKnife;


/**
 * @author matao
 */
public abstract class BaseActivity extends AppCompatActivity implements NetStatusMonitor {

    private static final String TAG = "BaseActivity";
    protected View mRoot;
    private NetworkReceiver mNetworkReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mRoot = buildView();
        setContentView(mRoot);

        registerNetworkReceiver();
        // 添加到Activity工具类
        ActivityManager.getInstance().addActivity(this);
        BusProvider.getInstance().register(this);

        //ButterKnife
        ButterKnife.bind(this);

        initView();

        initData();
    }


    public View buildView() {
        return LayoutInflater.from(this).inflate(getRootViewRes(), null);
    }


    /**
     * 进行获取根部的View
     *
     * @return RootView
     */
    public abstract int getRootViewRes();


    /**
     * 进行初始化相关的View
     */
    public abstract void initView();


    /**
     * 进行初始化相关的数据
     */
    public abstract void initData();


    /**
     * 为fragment 绑定通信接口
     *
     * @param tag fragment tag
     */
    public void setFunctionsForFragment(String tag) {
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logger.d(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(TAG, "onRestart: ");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(TAG, "onStart: ");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume: ");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop: ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
        unRegisterNetworkReceiver();
        BusProvider.getInstance().unregister(this);
        ActivityManager.getInstance().removeActivity(this);
    }


    @Override
    public void finish() {
        super.finish();
    }


    private void registerNetworkReceiver() {
        mNetworkReceiver = new NetworkReceiver();
        mNetworkReceiver.setNetStatusMonitor(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, intentFilter);
    }


    private void unRegisterNetworkReceiver() {
        if (mNetworkReceiver != null) {
            unregisterReceiver(mNetworkReceiver);
        }
    }

    @Override
    public void onNetChange(boolean netStatus) {
        Toast.makeText(getApplicationContext(), "NetworkStatus changed", Toast.LENGTH_SHORT).show();
    }

}
