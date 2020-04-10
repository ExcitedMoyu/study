package com.smasher.rejuvenation.base;

import android.view.View;

import androidx.annotation.NonNull;

import butterknife.ButterKnife;


/**
 * @author matao
 * @date 2019/4/4
 */
public class BaseRecyclerViewHolder<T> extends com.smasher.widget.base.BaseRecyclerViewHolder<T> {


    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void initButterKnife() {
        ButterKnife.bind(this, itemView);
    }
}
