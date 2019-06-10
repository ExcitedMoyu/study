package com.smasher.widget.helper;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;


import androidx.annotation.Nullable;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Author: wuzhendong
 * Date: 2018-07-11
 */
public class ThemeTransformHelper {
    public static final int TYPE_DAY_NIGHT = 1;
    public static final int TYPE_NIGHT_DAY = 2;

    private static final Comparator<String> sStringComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.length() < o2.length()) {
                return -1;
            } else if (o1.length() > o2.length()) {
                return 1;
            } else {
                return o1.compareTo(o2);
            }
        }
    };


    static ValueAnimator createArgbAnimator(int... values) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ValueAnimator.ofArgb(values);
        } else {
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(values);
            valueAnimator.setEvaluator(new TypeEvaluator() {
                @Override
                public Object evaluate(float fraction, Object startValue, Object endValue) {
                    int startInt = (Integer) startValue;
                    float startA = ((startInt >> 24) & 0xff) / 255.0f;
                    float startR = ((startInt >> 16) & 0xff) / 255.0f;
                    float startG = ((startInt >> 8) & 0xff) / 255.0f;
                    float startB = (startInt & 0xff) / 255.0f;

                    int endInt = (Integer) endValue;
                    float endA = ((endInt >> 24) & 0xff) / 255.0f;
                    float endR = ((endInt >> 16) & 0xff) / 255.0f;
                    float endG = ((endInt >> 8) & 0xff) / 255.0f;
                    float endB = (endInt & 0xff) / 255.0f;
                    // 应该和伽马矫正相关 sRGB使用的encoding gama 是1/2.2
                    // convert from sRGB to linear
                    startR = (float) Math.pow(startR, 2.2);
                    startG = (float) Math.pow(startG, 2.2);
                    startB = (float) Math.pow(startB, 2.2);

                    endR = (float) Math.pow(endR, 2.2);
                    endG = (float) Math.pow(endG, 2.2);
                    endB = (float) Math.pow(endB, 2.2);

                    // compute the interpolated color in linear space
                    float a = startA + fraction * (endA - startA);
                    float r = startR + fraction * (endR - startR);
                    float g = startG + fraction * (endG - startG);
                    float b = startB + fraction * (endB - startB);

                    // convert back to sRGB in the [0..255] range
                    a = a * 255.0f;
                    r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
                    g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
                    b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

                    return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
                }
            });
            return valueAnimator;
        }
    }

    /**
     * @param type {@link #TYPE_DAY_NIGHT} {@link #TYPE_NIGHT_DAY}
     */
    @Nullable
    public static AnimationDrawable2 loadDayNightAnimationDrawable(Context context, int type, long duration) {
        try {
            BitmapFactory.Options options = null;
            if(context.getResources().getDisplayMetrics().density < 2){
                //图片是按二倍图给的，屏幕过小需要设置采样
                float sampleSize = 2f / context.getResources().getDisplayMetrics().density;
                options = new BitmapFactory.Options();
                options.inSampleSize = (int) sampleSize;
            }
            AssetManager assetManager = context.getAssets();
            Resources resources = context.getResources();
            String dir;
            if (type == TYPE_DAY_NIGHT) {
                dir = "images/daynight";
            } else {
                dir = "images/nightday";
            }
            String[] fileNames = assetManager.list(dir);
            if (fileNames == null || fileNames.length == 0) {
                return null;
            }
            Arrays.sort(fileNames, sStringComparator);
            int frameDuration = (int) (duration / fileNames.length);
            AnimationDrawable2 animationDrawable = new AnimationDrawable2();
            for (String fileName : fileNames) {
                InputStream stream = assetManager.open(dir + File.separator + fileName);
                try {
                    Bitmap frame = BitmapFactory.decodeStream(stream, null, options);
                    animationDrawable.addFrame(new BitmapDrawable(resources, frame), frameDuration);
                } finally {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        //ignore
                    }
                }
            }
            return animationDrawable;
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
