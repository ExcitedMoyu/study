package com.smasher.oa.core.utils;

import android.util.DisplayMetrics;

import com.smasher.oa.core.other.ApplicationContext;


public class DensityUtil {
    // 根据手机的分辨率dip --> px
    public static int dip2px(float dpValue) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    // 根据手机的分辨率 px --> dp
    public static int px2dip(float pxValue) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float sp2px(float sp) {
        final float scale = ApplicationContext.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static float getDensity() {
        return ApplicationContext.getInstance().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW() {
        DisplayMetrics dm = ApplicationContext.getInstance().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH() {
        DisplayMetrics dm = ApplicationContext.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
