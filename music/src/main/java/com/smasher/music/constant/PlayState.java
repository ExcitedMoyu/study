package com.smasher.music.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author matao
 * @date 2019/5/25
 */
@IntDef({Constant.MUSIC_STATE_PLAY, Constant.MUSIC_STATE_PAUSE})
@Retention(RetentionPolicy.SOURCE)
public @interface PlayState {
}
