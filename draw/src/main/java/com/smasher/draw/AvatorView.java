package com.smasher.draw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class AvatorView extends View {

    Paint mPaint;
    Bitmap mBitmap;
    BitmapShader mBitmapShader;
    Matrix mMatrix;


    public AvatorView(Context context) {
        super(context);
        init(context, null);
    }

    public AvatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public AvatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        mPaint = new Paint();
        mMatrix = new Matrix();
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    public void setBitmapDrawable() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scale = (float) getWidth() / mBitmap.getWidth();
        mMatrix.setScale(scale, scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);

        float half = getWidth() / 2f;
        canvas.drawCircle(half, half, getWidth() / 2f, mPaint);
    }
}
