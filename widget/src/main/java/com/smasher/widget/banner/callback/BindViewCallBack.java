package com.smasher.widget.banner.callback;

import android.view.View;

/**
 * Created by lin on 2018/1/4.
 * 绑定view回调
 */
public interface BindViewCallBack<V extends View, T> {
    /**
     * 绑定view
     *
     * @param view     自定义的布局
     * @param data     banner数据
     * @param position 位置
     */
    void bindView(V view, T data, int position);
}
