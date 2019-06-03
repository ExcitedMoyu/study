package com.smasher.media.adapter;

import android.view.View;

/**
 * @author matao
 * @date 2019/5/10
 */
public interface OnItemClickListener {
    /**
     * listener
     *
     * @param view     view
     * @param position position
     */
    void onClick(View view, int position);
}
