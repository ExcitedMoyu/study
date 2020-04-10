package com.smasher.downloader.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Smasher
 * on 2020/3/11 0011
 */
@IntDef({RequestType.COMMAND_DOWNLOAD, RequestType.COMMAND_PAUSE,
        RequestType.COMMAND_INSTALL, RequestType.COMMAND_OPEN})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestType {
     int COMMAND_DOWNLOAD = 0;
     int COMMAND_PAUSE = 1;
     int COMMAND_INSTALL = 2;
     int COMMAND_OPEN = 3;
}
