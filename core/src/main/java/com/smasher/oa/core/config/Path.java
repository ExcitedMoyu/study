package com.smasher.oa.core.config;


import android.os.Environment;

import java.io.File;


/**
 * @author lin
 * @date 2017/7/5
 */

public class Path {
    public static final String NAME = "XCM_OA";

    static {
        AppPath.init(NAME);
    }

    public static String getRootPath() {
        return AppPath.getRootPath();
    }

    public static String getCachePath() {
        return AppPath.getSubPath("cache");
    }

    public static String getSplashPath() {
        return AppPath.getSubPath("splash");
    }

    public static String getImagePath() {
        return AppPath.getSubPath("image");
    }

    public static String getLogPath() {
        return AppPath.getSubPath("log");
    }

    public static String getDownloadPath() {
        return AppPath.getSubPath("log");
    }


    public static String getSDCardDBFilePath() {
        return AppPath.getRootPath() + NAME;
    }

    public static String getSDCardConfigFilePath() {
        return AppPath.getRootPath() + "Config";
    }

    public static String getSDCardQDHttpLogDBFilePath() {
        return AppPath.getRootPath() + "HttpLog";
    }


    /**
     * 获取系统图片地址
     *
     * @return String
     */
    public static String getDCIMPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    /**
     * 获取系统图片下app子文件夹地址
     *
     * @return String
     */
    public static String getAppDCIMPath() {
        String result = getDCIMPath() + "/" + NAME;
        File dir = new File(result);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir.getAbsolutePath() + "/";
    }


}
