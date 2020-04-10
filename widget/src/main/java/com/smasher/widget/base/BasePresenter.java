package com.smasher.widget.base;


import androidx.annotation.Nullable;

import com.smasher.widget.base.IBaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 通过弱引用和Activity、Fragment的生命周期解决可能的内存泄漏问题
 * BasePresenter是一个泛型类，泛型类型为View角色要实现的接口
 *
 * @author matao
 */
public abstract class BasePresenter<T extends IBaseView> {

    public static final String TAG = "Presenter";

    /**
     * View接口类型的弱引用
     */
    protected Reference<T> mViewRef;

    protected void attachView(T view) {
        // 建立关联
        mViewRef = new WeakReference<T>(view);

        if (isViewAttached()) {
            if (getView() != null) {
                // 建立关联
                getView().setPresenter(this);
            }
        }
    }

    @Nullable
    protected T getView() {
        if (mViewRef != null) {
            return mViewRef.get();
        }

        return null;
    }

    protected boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() { // 在Activity的onDestroy()中调用
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public void start() {

    }
}
