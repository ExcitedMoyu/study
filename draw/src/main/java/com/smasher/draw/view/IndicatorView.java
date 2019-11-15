package com.smasher.draw.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.smasher.core.utils.DensityUtil;


/**
 * @author warrior
 */
public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";
    private Paint paint;
    private int mMax;
    private int currentPosition;

    private int selectorColor = Color.RED;
    private int unSelectorColor = Color.WHITE;

    private int radiusEn = 3;
    private int radiusUnEn = 3;
    private int spacing = 6;

    private Context mContext;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(0, 0);
    }

    public void init(int position, int max) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (max != 0) {
            mMax = max;
            currentPosition = position;
        } else {
            mMax = max;
            currentPosition = position;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void circle() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mMax);
        animator.setDuration(3000);
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.addUpdateListener(animation ->
                setPosition((Integer) animation.getAnimatedValue()));
        animator.start();
    }

    public void setColorResourceId(int selectorColorId, int unSelectorColorId) {
        this.selectorColor = ContextCompat.getColor(mContext, selectorColorId);
        this.unSelectorColor = ContextCompat.getColor(mContext, unSelectorColorId);
    }

    public void setPosition(int position) {
        currentPosition = position;
        invalidate();
    }

    public void setRadiu(int radiusEn, int radiusUnEn) {
        this.radiusEn = radiusEn;
        this.radiusUnEn = radiusUnEn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = 0;
        int y = radiusUnEn + 5;

        for (int i = 0; i < mMax; i++) {
            x = x + 2 * radiusUnEn + spacing;
            paint.setColor(i == currentPosition ? selectorColor : unSelectorColor);
            canvas.drawCircle(DensityUtil.dip2px(mContext, x), DensityUtil.dip2px(mContext, y),
                    DensityUtil.dip2px(mContext, radiusUnEn), paint);
        }
    }

    /**
     * 计算控件的高宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMax = radiusEn > radiusUnEn ? radiusEn : radiusUnEn;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        Log.d(TAG, "onMeasure: width:" + width);
        int widthTemp = DensityUtil.dip2px(mContext, (2 * radiusUnEn + spacing) * mMax + 2 * radiusEn - radiusUnEn + 13);
        Log.d(TAG, "onMeasure: widthTemp:" + widthTemp);
        width = width < widthTemp ? width : widthTemp;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: height:" + height);
        int heightTemp = DensityUtil.dip2px(mContext, 2 * heightMax + 13);
        Log.d(TAG, "onMeasure: heightTemp:" + heightTemp);
        height = height < heightTemp ? height : heightTemp;

        setMeasuredDimension(width, height);
    }
}
