package com.smasher.media.helper;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author matao
 * @date 2019/6/12
 */
public class AnimationHelper {



    public void playStateChangeAnimation(ImageView target, int resource) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorOutAlpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.2f);
        animatorOutAlpha.setDuration(400);
        animatorOutAlpha.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 0.6f);
        animatorScaleY.setDuration(400);
        animatorScaleY.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 0.6f);
        animatorScaleX.setDuration(400);
        animatorScaleX.setInterpolator(new LinearInterpolator());

        ObjectAnimator animatorInScaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.6f, 1.0f);
        animatorInScaleY.setDuration(400);
        animatorInScaleY.setStartDelay(300);
        animatorInScaleY.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorInScaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.6f, 1.0f);
        animatorInScaleX.setDuration(400);
        animatorInScaleX.setStartDelay(300);
        animatorInScaleX.setInterpolator(new LinearInterpolator());
        ObjectAnimator animatorIn = ObjectAnimator.ofFloat(target, "alpha", 0.6f, 1.0f);
        animatorIn.setStartDelay(300);
        animatorIn.setDuration(400);
        animatorIn.setInterpolator(new AccelerateInterpolator());
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setImageResource(resource);
            }
        });
        set.playTogether(animatorOutAlpha, animatorScaleX, animatorScaleY, animatorInScaleX, animatorInScaleY, animatorIn);
        set.start();
    }




    public void modeChangeAnimation(ImageView target, int resource) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorOutAlpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.2f);
        animatorOutAlpha.setDuration(400);
        animatorOutAlpha.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorOut = ObjectAnimator.ofFloat(target, "rotation", 0.0f, 360.0f);
        animatorOut.setDuration(400);
        animatorOut.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorIn = ObjectAnimator.ofFloat(target, "alpha", 0.2f, 1.0f);
        animatorIn.setStartDelay(300);
        animatorIn.setDuration(400);
        animatorIn.setInterpolator(new AccelerateInterpolator());
        animatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setImageResource(resource);
            }
        });
        set.playTogether(animatorOutAlpha, animatorOut, animatorIn);
        set.start();
    }

}
