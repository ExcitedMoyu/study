package com.smasher.draw.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.smasher.draw.R;


/**
 * @author matao
 * @date 2019/5/7
 */
public class BlurMaskFilterView extends ViewGroup {

    private static final String TAG = "BlurMaskFilterView";
    private Paint mPaint;
    private RectF rectF;
    private int mColor;
    private int mTextColor;

    private int invented;
    private int radius;

    private MaskFilter mMaskFilter;
    private AppCompatTextView mTextView;
    private int mTextPadding;
    private int mTextPaddingLeft;
    private int mTextPaddingRight;
    private int mTextPaddingTop;
    private int mTextPaddingBottom;

    private String mText;

    private int mPressedColor;
    private int mNormalColor;


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

        int defaultPadding = 26;
        int defaultColor = Color.parseColor("#ED424B");
        int defaultInvented = 53;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BlurMaskFilterView);

        mTextPadding = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_text_padding, defaultPadding);
        mTextPaddingLeft = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_text_paddingLeft, defaultPadding);
        mTextPaddingRight = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_text_paddingRight, defaultPadding);
        mTextPaddingTop = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_text_paddingTop, defaultPadding);
        mTextPaddingBottom = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_text_paddingBottom, defaultPadding);

        mColor = array.getColor(R.styleable.BlurMaskFilterView_blur_color, defaultColor);
        mTextColor = array.getColor(R.styleable.BlurMaskFilterView_blur_text_color, defaultColor);
        mPressedColor = array.getColor(R.styleable.BlurMaskFilterView_blur_pressed_color, defaultColor);
        mNormalColor = array.getColor(R.styleable.BlurMaskFilterView_blur_normal_color, defaultColor);

        invented = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_width, defaultInvented);
        radius = array.getDimensionPixelSize(R.styleable.BlurMaskFilterView_blur_radius, defaultInvented);
        mText = array.getString(R.styleable.BlurMaskFilterView_blur_text);
        boolean enable = array.getBoolean(R.styleable.BlurMaskFilterView_blur_enable_state, false);

        array.recycle();


        mTextView = new AppCompatTextView(context);
        mTextView.setGravity(Gravity.CENTER);
        if (enable) {
            mTextView.setBackground(createDrawable());
        } else {
            mTextView.setBackgroundColor(Color.YELLOW);
        }
        mTextView.setTextColor(mTextColor);
        mTextView.setText(mText);
        addView(mTextView);


        mPaint = new Paint();
        rectF = new RectF();
        mMaskFilter = new BlurMaskFilter(invented, BlurMaskFilter.Blur.OUTER);


    }


    public StateListDrawable createDrawable() {
        float[] radiusArray = new float[8];
        for (int i = 0; i < 8; i++) {
            radiusArray[i] = radius;
        }

        StateListDrawable listDrawable = new StateListDrawable();

        RoundRectShape pressedShape = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable pressedDrawable = new ShapeDrawable(pressedShape);
        pressedDrawable.getPaint().setColor(mPressedColor);
        pressedDrawable.setPadding(mTextPaddingLeft, mTextPaddingTop, mTextPaddingRight, mTextPaddingBottom);
        int[] stateSet1 = new int[]{android.R.attr.state_pressed};
        listDrawable.addState(stateSet1, pressedDrawable);

        RoundRectShape normalShape = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable normalDrawable = new ShapeDrawable(normalShape);
        normalDrawable.getPaint().setColor(mNormalColor);
        normalDrawable.setPadding(mTextPaddingLeft, mTextPaddingTop, mTextPaddingRight, mTextPaddingBottom);
        int[] stateSet2 = new int[]{};
        listDrawable.addState(stateSet2, normalDrawable);

        return listDrawable;
    }


    public void setTextColor(int color) {
        if (mTextView != null) {
            mTextView.setTextColor(color);
        }
    }


    public void setTextColor(ColorStateList colorStateList) {
        if (mTextView != null) {
            mTextView.setTextColor(colorStateList);
        }
    }


    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (mTextView != null) {
            mTextView.setCompoundDrawables(left, top, right, bottom);
        }
    }


    public void setCompoundDrawablePadding(int padding) {
        if (mTextView != null) {
            mTextView.setCompoundDrawablePadding(padding);
        }
    }


//    public void setText(CharSequence text) {
//        mText = text.toString();
//        if (mTextView != null) {
//            mTextView.setText(mText);
//            invalidate();
//        }
//    }


    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        if (mTextView != null) {
            mTextView.setOnClickListener(l);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(mTextView, widthMeasureSpec, heightMeasureSpec);
        int width = onMeasureIWidth(widthMeasureSpec);
        int height = onMeasureIHeight(heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        setMeasuredDimension(width, height);
    }

    private int onMeasureIWidth(int oldMeasure) {
        int width = 0;
        int mode = MeasureSpec.getMode(oldMeasure);
        int size = MeasureSpec.getSize(oldMeasure);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                width = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int childWidth = mTextView.getMeasuredWidth();
                width = (getPaddingLeft() + invented * 2 + childWidth + getPaddingRight());
                break;
            default:
                break;
        }
        return width;
    }


    private int onMeasureIHeight(int oldMeasure) {
        int height = 0;
        int mode = MeasureSpec.getMode(oldMeasure);
        int size = MeasureSpec.getSize(oldMeasure);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                height = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int childHeight = mTextView.getMeasuredHeight();
                height = (getPaddingTop() + invented * 2 + childHeight + getPaddingBottom());
                break;
            default:
                break;
        }
        return height;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout: ");
        //+ mTextPaddingLeft + mTextPaddingRight
        int childWidth = mTextView.getMeasuredWidth();
        int childHeight = mTextView.getMeasuredHeight();


        int childLeft = invented;
        int childRight = getMeasuredWidth() - invented;
        int childTop = invented;
        int childBottom = getMeasuredHeight() - invented;

        Log.d(TAG, "onLayout: " + childLeft + "--" + childTop
                + "--" + childRight + "--" + childBottom);

        mTextView.layout(childLeft, childTop, childRight, childBottom);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");

        int childWidth = mTextView.getMeasuredWidth();
        int childHeight = mTextView.getMeasuredHeight();
//        Log.d(TAG, "onDraw: " + childWidth + "--" + childHeight);

        int childLeft = invented;
        int childRight = getMeasuredWidth() - invented;
        int childTop = invented;
        int childBottom = getMeasuredHeight() - invented;

//        Log.d(TAG, "onDraw: " + childLeft + "--" + childTop
//                + "--" + childRight + "--" + childBottom);
        rectF.set(childLeft, childTop, childRight, childBottom);

        mPaint.setColor(mColor);
        mPaint.setMaskFilter(mMaskFilter);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);
    }
}
