package com.smasher.rejuvenation.util;

import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.smasher.core.other.ApplicationContext;

/**
 * Toast相关工具类
 * @author moyu
 */
public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static Toast mToast;


    /**
     * 显示短时toast
     *
     * @param text 文本
     */
    public static void showShortToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时toast
     *
     * @param resId 资源Id
     */
    public static void showShortToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时toast
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShortToast(@StringRes int resId, Object... args) {
        showToast(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时toast
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShortToast(String format, Object... args) {
        showToast(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示长时toast
     *
     * @param text 文本
     */
    public static void showLongToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时toast
     *
     * @param resId 资源Id
     */
    public static void showLongToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时toast
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLongToast(@StringRes int resId, Object... args) {
        showToast(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示长时toast
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLongToast(String format, Object... args) {
        showToast(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示toast
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void showToast(@StringRes int resId, int duration) {
        showToast(ApplicationContext.getInstance().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示toast
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void showToast(@StringRes int resId, int duration, Object... args) {
        showToast(String.format(ApplicationContext.getInstance().getResources().getString(resId), args), duration);
    }

    /**
     * 显示toast
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void showToast(String format, int duration, Object... args) {
        showToast(String.format(format, args), duration);
    }


    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void showToast(CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(ApplicationContext.getInstance(), text, duration);
            TextView tv = mToast.getView().findViewById(android.R.id.message);
            tv.setTextSize(14);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }


    /**
     * 取消toast显示
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}