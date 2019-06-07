package com.smasher.media.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created on 2019/6/7.
 *
 * @author moyu
 */
@IntDef({ActionType.ACTION_NEXT, ActionType.ACTION_PREVIOUS})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
    public static final int ACTION_NEXT = 0;
    public static final int ACTION_PREVIOUS = 1;
}
