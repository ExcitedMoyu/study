package com.smasher.widget.base;


import android.view.View;

/**
 * @author moyu
 */
public class BaseCommonViewHolder<T> {

    protected T mItem;


    protected BaseCommonViewHolder(View view) {

    }

    public void setItem(T item) {
        mItem = item;
    }

    public void bindView() {

    }


}
