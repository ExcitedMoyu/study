package com.smasher.widget.banner.callback;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by lin on 2018/1/4.
 * 加载默认布局
 */
public class CreateViewCaller implements CreateViewCallBack<FrameLayout> {

    public static CreateViewCaller build() {
        return new CreateViewCaller();
    }

    public static AppCompatImageView findImageView(View view) {
        if (view instanceof ViewGroup) {
            return (AppCompatImageView) ((ViewGroup) view).getChildAt(0);
        }
        return null;
    }

    @Override
    public FrameLayout createView(Context context, ViewGroup parent, int viewType) {
        return createImageView(context);
    }


    private FrameLayout createImageView(Context context) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        FrameLayout layout = new FrameLayout(context);
        layout.setLayoutParams(params);

        AppCompatImageView view = new AppCompatImageView(context);
        view.setLayoutParams(params);
        view.setScaleType(AppCompatImageView.ScaleType.FIT_XY);

        layout.addView(view, params);
        return layout;
    }
}
