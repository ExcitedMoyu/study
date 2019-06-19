package com.smasher.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.smasher.widget.R;


/**
 * @author matao
 * @date 2019/5/7
 */
public class BlurMaskFilterView extends LinearLayout {

    private static final String TAG = "BlurMaskFilterView";
    private Paint mPaint;
    private RectF rectF;
    private int mColor;
    private int radius;
    private int invented;
    private MaskFilter mMaskFilter;

    public BlurMaskFilterView(Context context) {
        this(context, null);
    }

    public BlurMaskFilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurMaskFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);

        int defaultColor = Color.parseColor("#ED424B");
        int defaultInvented = 3;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BlurMaskFilterView);
        invented = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_width, defaultInvented);
        radius = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_radius, defaultInvented);
        mColor = array.getColor(R.styleable.BlurMaskFilterView_blur_color, defaultColor);
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);


        rectF = new RectF();

        float with = calculateBlurWidth();
        mMaskFilter = new BlurMaskFilter(with, BlurMaskFilter.Blur.SOLID);
        setPadding(0, 0, 0, 0);
    }

    private float calculateBlurWidth() {
        float blur = 0;
        float target = 9 / 10f;
        blur = target * invented;

        if (blur > 2) {
            blur = invented - 2;
        }
        return blur;
    }


    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        left = left + invented;
        top = top + invented;
        right = right + invented;
        bottom = bottom + invented;
        super.setPadding(left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: " + invented);
        super.onDraw(canvas);
        int childLeft = invented;
        int childRight = getMeasuredWidth() - invented;
        int childTop = invented;
        int childBottom = getMeasuredHeight() - invented;

        rectF.set(childLeft, childTop, childRight, childBottom);


        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mColor);
        mPaint.setMaskFilter(mMaskFilter);
        mPaint.setStrokeWidth(5);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);
//        canvas.drawRect(rectF,mPaint);
    }
}
