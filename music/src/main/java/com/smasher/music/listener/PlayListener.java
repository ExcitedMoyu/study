package com.smasher.music.listener;

import com.smasher.music.constant.PlayState;
import com.smasher.music.entity.MediaInfo;

/**
 * @author matao
 * @date 2019/5/24
 */
public interface PlayListener {

    void onPlayItemChanged(MediaInfo item);

    void onPlayStateChanged(@PlayState int state);
}
