package com.smasher.music.helper;

import com.smasher.music.constant.Constant;
import com.smasher.music.entity.RequestInfo;

/**
 * @author matao
 * @date 2019/5/25
 */
public class RequestHelper {

    public RequestHelper() {
    }

    public void packState(RequestInfo requestInfo, int state) {
        switch (state) {
            case Constant.MUSIC_STATE_PAUSE:
                requestInfo.setCommandType(RequestInfo.COMMAND_PAUSE);
                break;
            case Constant.MUSIC_STATE_PLAY:
                requestInfo.setCommandType(RequestInfo.COMMAND_PLAY);
                break;
            default:
                break;
        }
    }
}
