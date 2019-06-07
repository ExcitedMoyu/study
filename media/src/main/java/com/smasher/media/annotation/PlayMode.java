package com.smasher.media.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created on 2019/6/7.
 *
 * @author moyu
 */
@IntDef()
@Retention(RetentionPolicy.SOURCE)
public @interface PlayMode {
    public static final int PLAY_MODE_NONE = 0;
    public static final int PLAY_MODE_SINGLE = 1;
    public static final int PLAY_MODE_CIRCULATE = 0;

}
