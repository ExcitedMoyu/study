package com.smasher.draw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;
import com.smasher.core.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 节奏
 *
 * @author matao
 * @date 2019/6/13
 */
public class RhythmView extends View {

    private static final String TAG = "RhythmView";
    private int count;
    private int singleWidth;
    private Paint mPaint;
    private ArrayList<Rect> mRects;
    private ArrayList<Integer> mVaryingHeights;
    private AnimatorSet animatorSet;

    public RhythmView(Context context) {
        this(context, null);
    }

    public RhythmView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RhythmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        count = 5;
        singleWidth = DensityUtil.dip2px(getContext(), 5);
        mPaint = new Paint();
        initRects();
    }

    private void initRects() {
        mRects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Rect rect = new Rect();
            mRects.add(rect);
        }
        mVaryingHeights = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Integer item = 0;
            mVaryingHeights.add(item);
        }
    }


    public void cancelAnimation() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public void showAnimation() {
        int height = getMeasuredHeight();

        int min = (int) (height * 0.2);
        int max = (int) (height * 0.8);

        Log.d(TAG, "showAnimation: " + min + "-" + max);

        animatorSet = new AnimatorSet();

        ValueAnimator animatorP2 = ValueAnimator.ofInt(min, max);
        animatorP2.addUpdateListener(mUpdateListenerP2);
        animatorP2.setRepeatCount(ValueAnimator.INFINITE);
        animatorP2.setRepeatMode(ValueAnimator.REVERSE);
        animatorP2.setStartDelay(0);

        ValueAnimator animatorP1 = ValueAnimator.ofInt(min, max);
        animatorP1.addUpdateListener(mUpdateListenerP1);
        animatorP1.setRepeatCount(ValueAnimator.INFINITE);
        animatorP1.setRepeatMode(ValueAnimator.REVERSE);
        animatorP1.setStartDelay(100);

        ValueAnimator animator = ValueAnimator.ofInt(min, max);
        animator.addUpdateListener(mUpdateListener);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setStartDelay(200);


        ValueAnimator animatorN1 = ValueAnimator.ofInt(min, max);
        animatorN1.addUpdateListener(mUpdateListenerN1);
        animatorN1.setRepeatCount(ValueAnimator.INFINITE);
        animatorN1.setRepeatMode(ValueAnimator.REVERSE);
        animatorN1.setStartDelay(300);

        ValueAnimator animatorN2 = ValueAnimator.ofInt(min, max);
        animatorN2.addUpdateListener(mUpdateListenerN2);
        animatorN2.setRepeatCount(ValueAnimator.INFINITE);
        animatorN2.setRepeatMode(ValueAnimator.REVERSE);
        animatorN2.setStartDelay(400);

        animatorSet.playTogether(animatorP2, animatorP1, animator, animatorN1, animatorN2);
        animatorSet.setDuration(600);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }


    private ValueAnimator.AnimatorUpdateListener mUpdateListenerP2 = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mVaryingHeights.set(0, (int) animation.getAnimatedValue());

        }
    };


    private ValueAnimator.AnimatorUpdateListener mUpdateListenerP1 = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mVaryingHeights.set(1, (int) animation.getAnimatedValue());
        }
    };

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mVaryingHeights.set(2, (int) animation.getAnimatedValue());
            invalidate();
        }
    };


    private ValueAnimator.AnimatorUpdateListener mUpdateListenerN1 = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mVaryingHeights.set(3, (int) animation.getAnimatedValue());
        }
    };

    private ValueAnimator.AnimatorUpdateListener mUpdateListenerN2 = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mVaryingHeights.set(4, (int) animation.getAnimatedValue());
        }
    };


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.BLUE);

        Log.d(TAG, "onDraw: " + mVaryingHeights.toString());
        for (int i = 0; i < count; i++) {
            Rect temp = mRects.get(i);
            int height = mVaryingHeights.get(i);
            int position = getPosition(i);
            setSpecs(temp, height, position);
            canvas.drawRect(temp, mPaint);
        }
    }

    private int getPosition(int index) {
        int target = 0;
        if (count % 2 > 0) {
            int center = count / 2;
            target = index - center;
        } else {
            int center = count / 2;
        }
        return target;
    }

    private void setSpecs(Rect rect, int varyHeight, int position) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int centerWidth = width / 2 + position * (singleWidth + 15);
        int centerHeight = height / 2;

        int left = centerWidth - singleWidth / 2;
        int right = centerWidth + singleWidth / 2;
        int top = centerHeight - varyHeight / 2;
        int bottom = centerHeight + varyHeight / 2;
        rect.set(left, top, right, bottom);
        Log.d(TAG, "setSpecs: " + varyHeight + "-" + position);
    }

}
