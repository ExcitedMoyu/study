package com.smasher.widget.helper;

import android.graphics.drawable.AnimationDrawable;

/**
 * Date: 2018-07-11
 *
 * @author matao
 */
public class AnimationExDrawable extends AnimationDrawable {
    public interface IAnimationFinishListener {
        /**
         * onAnimationFinished
         */
        void onAnimationFinished();
    }

    private boolean finished = false;
    private IAnimationFinishListener animationFinishListener;

    IAnimationFinishListener getAnimationFinishListener() {
        return animationFinishListener;
    }

    public void setAnimationFinishListener(IAnimationFinishListener animationFinishListener) {
        this.animationFinishListener = animationFinishListener;
        finished = false;
    }

    @Override
    public boolean selectDrawable(int idx) {
        boolean ret = super.selectDrawable(idx);

        if ((idx != 0) && (idx == getNumberOfFrames() - 1)) {
            if (!finished) {
                finished = true;
                if (animationFinishListener != null) {
                    animationFinishListener.onAnimationFinished();
                }
            }
        }

        return ret;
    }

    @Override
    public void stop() {
        if (isRunning() && !finished) {
            finished = true;
            if (animationFinishListener != null) {
                animationFinishListener.onAnimationFinished();
            }
        }
        super.stop();
    }
}
