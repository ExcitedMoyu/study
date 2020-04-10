package com.smasher.downloader.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Smasher
 * on 2020/3/11 0011
 */

@IntDef({DownloadType.DOWN_LOAD_TYPE_COMMON, DownloadType.DOWN_LOAD_TYPE_APK})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadType {
    /**
     * 普通文件
     */
     int DOWN_LOAD_TYPE_COMMON = 100;

    /**
     * apk安装包文件
     */
     int DOWN_LOAD_TYPE_APK = 101;
}
