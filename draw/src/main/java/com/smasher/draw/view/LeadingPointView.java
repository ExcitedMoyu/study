package com.smasher.draw.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.smasher.oa.core.utils.DensityUtil;


public class LeadingPointView extends View {
    Paint paint;
    int MaxN;
    int currentPosition;

    int selectorColor = Color.RED;
    int unSelectorColor = Color.WHITE;

    int radiusEn = 3;
    int radiusUnEn = 3;
    int spacing = 6;

    Context mContext;

    public LeadingPointView(Context context) {
        super(context);
        mContext = context;
        init(0, 0);
    }

    public LeadingPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(0, 0);
    }

    public LeadingPointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(0, 0);
    }

    public void init(int Position, int Max) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (Max != 0) {
            MaxN = Max;
            currentPosition = Position;
        } else {
            MaxN = Max;
            currentPosition = Position;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void circle() {
        ValueAnimator animator = ValueAnimator.ofInt(0, MaxN);
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

    public void setRadiu(int radiuEn, int radiuUnEn) {
        this.radiusEn = radiuEn;
        this.radiusUnEn = radiuUnEn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        int x = 0;
        int y = radiusEn > radiusUnEn ? radiusEn + 5 : radiusUnEn + 5;

        for (int i = 0; i < MaxN; i++) {
            if (i == currentPosition) {
                paint.setColor(selectorColor);
                x = x + radiusEn + radiusUnEn + spacing;
                canvas.drawCircle(CommonUtil.dip2px(context, x),
                        CommonUtil.dip2px(context, y),
                        CommonUtil.dip2px(context, radiusEn), paint);
            } else {
                x = x + 2 * radiusUnEn + spacing;
                paint.setColor(unSelectorColor);
                canvas.drawCircle(CommonUtil.dip2px(context, x),
                        CommonUtil.dip2px(context, y),
                        CommonUtil.dip2px(context, radiusUnEn), paint);
            }
        }
        */
        int x = 0;
        int y = radiusUnEn + 5;

        for (int i = 0; i < MaxN; i++) {
            x = x + 2 * radiusUnEn + spacing;
            if (i == currentPosition) {
                paint.setColor(selectorColor);
            } else {
                paint.setColor(unSelectorColor);
            }
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
        int width = (2 * radiusUnEn + spacing) * MaxN + 2 * radiusEn - radiusUnEn + 13;
        int heightTemp = radiusEn > radiusUnEn ? radiusEn : radiusUnEn;
        int height = 2 * heightTemp + 13;
        setMeasuredDimension(DensityUtil.dip2px(mContext, width), DensityUtil.dip2px(mContext, height));
    }
}
