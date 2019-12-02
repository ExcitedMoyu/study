package com.smasher.widget.stepper;

import android.view.View;

/**
 * @author Sai
 * @date 16/1/15
 */
public interface ValueChangeListener {

    /**
     * 数值
     *
     * @param view  view
     * @param value value
     */
    void onValueChange(View view, int value);
}
