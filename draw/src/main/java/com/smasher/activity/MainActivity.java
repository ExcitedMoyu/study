package com.smasher.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.smasher.draw.AliPaySuccessView;
import com.smasher.draw.BlurMaskFilterView;
import com.smasher.draw.CanvasView;
import com.smasher.draw.R;
import com.smasher.util.DensityUtil;
import com.smasher.util.TintUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author matao
 */
public class MainActivity extends AppCompatActivity {


    @BindView(R.id.blurMask)
    BlurMaskFilterView blurMask;

    Unbinder mUnbinder;
    @BindView(R.id.canvasView)
    CanvasView canvasView;
    @BindView(R.id.success)
    AliPaySuccessView success;
    @BindView(R.id.blurMask1)
    BlurMaskFilterView blurMask1;
    @BindView(R.id.test)
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);


        Drawable arrowOrigin = ContextCompat.getDrawable(this, R.drawable.icon_arrow_down_black);
        Drawable arrow = null;
        if (arrowOrigin != null) {
            arrow = TintUtil.tintDrawable(arrowOrigin, Color.WHITE);
        }
        if (arrow != null) {
            int width = DensityUtil.dip2px(12);
            int height = DensityUtil.dip2px(8);
            arrow.setBounds(0, 0, width, height);
        }
        Drawable background = ContextCompat.getDrawable(this, R.drawable.selector_blurmask_button);

//        blurMask.setCompoundDrawablePadding(DensityUtil.dip2px(10));
//        blurMask.setCompoundDrawables(null, null, arrow, null);
//        blurMask.setText("测试一下");

        blurMask1.setCompoundDrawablePadding(DensityUtil.dip2px(40));
        blurMask1.setCompoundDrawables(null, null, arrow, null);
        float[] radiusArray = new float[8];
        for (int i = 0; i < 8; i++) {
            radiusArray[i] = DensityUtil.dip2px(20);
        }
        int mTextPaddingLeft = DensityUtil.dip2px(15);
        StateListDrawable listDrawable = new StateListDrawable();
        RoundRectShape normalShape = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable normalDrawable = new ShapeDrawable(normalShape);
        normalDrawable.getPaint().setColor(Color.parseColor("#ED424B"));
        MaskFilter mMaskFilter = new BlurMaskFilter(mTextPaddingLeft, BlurMaskFilter.Blur.OUTER);
        normalDrawable.getPaint().setMaskFilter(mMaskFilter);
        normalDrawable.setPadding(mTextPaddingLeft, mTextPaddingLeft, mTextPaddingLeft, mTextPaddingLeft);
        int[] stateSet2 = new int[]{};
        listDrawable.addState(stateSet2, normalDrawable);
        test.setBackground(listDrawable);
        test.setText("测试一下");
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @OnClick({R.id.blurMask, R.id.blurMask1})
    public void onViewClicked() {
        success.showAnimation();
        Animation transAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                1f
        );
        transAnimation.setRepeatMode(Animation.REVERSE);
        transAnimation.setRepeatCount(5);


        Animation alpha = new AlphaAnimation(0.5f, 1f);
        alpha.setRepeatMode(Animation.REVERSE);
        alpha.setRepeatCount(5);


        Animation rotate = new RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f

        );
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(5);


        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(500);
        set.setFillEnabled(true);
        set.setFillAfter(true);
//        set.addAnimation(transAnimation);
        set.addAnimation(alpha);
        set.addAnimation(rotate);
//        blurMask.startAnimation(set);

        AnimatorSet set1 = new AnimatorSet();
        ObjectAnimator trans = ObjectAnimator.ofFloat(
                blurMask,
                "translationX",
                blurMask.getTranslationX(), 500f, blurMask.getTranslationX());
        trans.setRepeatMode(ObjectAnimator.REVERSE);
        trans.setRepeatCount(ObjectAnimator.INFINITE);

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                blurMask,
                "rotation",
                blurMask.getRotation(), 180, blurMask.getRotation()
        );
        rotateAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        set1.playTogether(trans, rotateAnimator);
        set1.setDuration(2000);
        set1.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
