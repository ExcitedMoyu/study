package com.smasher.widget.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.oa.core.activityoptions.ActivityManager;
import com.smasher.oa.core.other.BusProvider;
import com.smasher.widget.receiver.NetworkReceiver;
import com.smasher.widget.receiver.NetworkReceiver.NetStatusMonitor;


/**
 * @author matao
 */
public abstract class BaseActivity extends AppCompatActivity implements NetStatusMonitor {

    protected View mRoot;
    protected int mPageIndex = 1;
    private NetworkReceiver mNetworkReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mRoot = getRootView();
        setContentView(mRoot);

        registerNetworkReceiver();
        // 添加到Activity工具类
        ActivityManager.getInstance().addActivity(this);
        BusProvider.getInstance().register(this);

        initView();

        initData();
    }


    /**
     * 为fragment 绑定通信接口
     *
     * @param tag fragment tag
     */
    public abstract void setFunctionsForFragment(String tag);

    /**
     * 进行获取根部的View
     *
     * @return RootView
     */
    public abstract View getRootView();

    /**
     * 进行初始化相关的View
     */
    public abstract void initView();

    /**
     * 进行初始化相关的数据
     */
    public abstract void initData();


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
