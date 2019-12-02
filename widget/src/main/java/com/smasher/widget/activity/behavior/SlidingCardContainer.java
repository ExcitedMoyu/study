package com.smasher.widget.activity.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.smasher.widget.R;

/**
 * Created on 2019/6/5.
 *
 * @author moyu
 */
public class SlidingCardContainer extends CoordinatorLayout {
    public SlidingCardContainer(@NonNull Context context) {
        this(context, null);
    }

    public SlidingCardContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingCardContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_behavior,this);
    }


}
