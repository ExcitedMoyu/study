package com.smasher.downloader.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Smasher
 * on 2020/3/11 0011
 */
@IntDef({State.JS_STATE_NORMAL, State.JS_STATE_WAIT,
        State.JS_STATE_DOWNLOAD_PRE, State.JS_STATE_GET_TOTAL,
        State.JS_STATE_DOWNLOADING, State.JS_STATE_FINISH,
        State.JS_STATE_PAUSE, State.JS_STATE_FAILED,
        State.JS_STATE_INSTALLED})
@Retention(RetentionPolicy.SOURCE)
public @interface State {

    /**
     * 默认状态
     */
    int JS_STATE_NORMAL = 0;


    /**
     * 任务排队
     */
    int JS_STATE_WAIT = 1;


    /**
     * 下载准备中
     */
    int JS_STATE_DOWNLOAD_PRE = 2;


    /**
     * 下载准备中
     */
    int JS_STATE_GET_TOTAL = 3;


    /**
     * 下载中
     */
    int JS_STATE_DOWNLOADING = 4;


    /**
     * 暂停
     */
    int JS_STATE_PAUSE = 5;


    /**
     * 下载完成
     */
    int JS_STATE_FINISH = 6;


    /**
     * 失败
     */
    int JS_STATE_FAILED = 7;


    /**
     * 已安装
     */
    int JS_STATE_INSTALLED = 8;

}
