package com.smasher.widget.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.oa.core.activityoptions.ActivityManager;
import com.smasher.oa.core.other.BusProvider;

import java.lang.reflect.Method;


/**
 * @author matao
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected View mRoot;
    protected int mPageIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // 添加到Activity工具类
        ActivityManager.getInstance().addActivity(this);

        mRoot = getRootView();
        setContentView(mRoot);
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
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        ActivityManager am = ActivityManager.getInstance();
        am.removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
    }


    //获取是否存在NavigationBar
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                //不存在虚拟按键
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                //存在虚拟按键
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }


    /**
     * 隐藏虚拟按键
     */
    public static void hideBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE |
                        // Hide the nav bar and status bar
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

    }

    /**
     * 显示虚拟按键
     */
    public static void showBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
