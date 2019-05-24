package com.smasher.music.constant;

import java.lang.Enum;
/**
 * @author matao
 * @date 2019/5/25
 */
public enum PlayStateEnum {
    /**
     *
     */
    REALEASE(0), PLAY(1), STOP(2);

    private int state;

    PlayStateEnum(int state) {
        state = state;
    }

    public int getState() {
        return state;
    }}
