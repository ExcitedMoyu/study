package com.smasher.draw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * @author matao
 * @date 2019/5/9
 */
public class SuccessView extends View {

    private static final String TAG = "AliPaySuccessView";
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Path mCirclePath;
    private Path mSuccessPath;
    private Path mDstPath;

    private float mCurAnimValue;
    private float mSuccessValue;

    private int mRadius;


    public SuccessView(Context context) {
        this(context, null);
    }

    public SuccessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public SuccessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mRadius = 50;
        mPaint = new Paint();
        mPathMeasure = new PathMeasure();
        mCirclePath = new Path();
        mSuccessPath = new Path();
        mDstPath = new Path();
    }


    public void showAnimation() {

        clear();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(animation -> {
            mCurAnimValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.setDuration(1000);


        ValueAnimator animator1 = ValueAnimator.ofFloat(0, 1);
        animator1.addUpdateListener(animation -> {
            mSuccessValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator1.setDuration(500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator, animator1);
        animatorSet.start();
    }


    boolean clear;

    public void clear() {
        clear = true;
        mCurAnimValue = 0f;
        mSuccessValue = 0f;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int dx = getMeasuredWidth() / 2;
        int dy = getMeasuredHeight() / 2;

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);

        mCirclePath.addCircle(dx, dy, mRadius, Path.Direction.CW);
        mPathMeasure.setPath(mCirclePath, true);

        mDstPath.reset();
        float stop = mPathMeasure.getLength() * mCurAnimValue;
        mPathMeasure.getSegment(0, stop, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);

        int distanceLeftOffset = -2 * mRadius / 5;
        int distanceRightOffset = 2 * mRadius / 5;
        int distanceTopOffset = -2 * mRadius / 5;
        int distanceBottomOffset = 2 * mRadius / 5;
        int distanceGapOffset = mRadius / 15;

        mSuccessPath.moveTo(dx + distanceLeftOffset, dy - distanceGapOffset);
        mSuccessPath.lineTo(dx - distanceGapOffset, dy + distanceBottomOffset);
        mSuccessPath.lineTo(dx + distanceRightOffset, dy + distanceTopOffset);

        mDstPath.reset();
        mPathMeasure.nextContour();
        mPathMeasure.setPath(mSuccessPath, false);
        float current = mPathMeasure.getLength() * mSuccessValue;
        mPathMeasure.getSegment(0, current, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);

    }
}
