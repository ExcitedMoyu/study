package com.xcm.oa.core.config;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;


import com.xcm.oa.core.other.ApplicationContext;

import java.io.File;

/**
 * @author moyu
 * @date 2017/3/13
 */

public class AppPath {
    private static String RootPath = ApplicationContext.getInstance().getPackageName();

    public static void init(String rootPath) {
        RootPath = rootPath;
    }

    public static String getRootPath() {
        String uniqueName = "/" + RootPath + "/";
        Application app = ApplicationContext.getInstance();
        File path = app.getFilesDir();
        String result = (path == null ? "/data/data/" + app.getPackageName() + "/files" : path.getAbsolutePath()) + uniqueName;
        try {
            String state = Environment.getExternalStorageState();
            if (!TextUtils.isEmpty(state) && Environment.MEDIA_MOUNTED.equals(state)) {
                File externalPath = Environment.getExternalStorageDirectory();
                if (externalPath != null && externalPath.exists() && externalPath.canWrite()) {
                    result = externalPath.getAbsolutePath() + uniqueName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir = new File(result);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return result;
    }

    public static String getSubPath(String subName) {
        String result = getRootPath() + "/" + subName;
        File dir = new File(result);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + "/";
    }

    public static String getCachePath() {
        return getSubPath("cache");
    }

    public static String getDownloadPath() {
        return getSubPath("download");
    }

    public static String getLogPath() {
        return getSubPath("log");
    }

    public static String getImagePath() {
        return getSubPath("image");
    }

    public static String getFontsPath() {
        return getSubPath("fonts");
    }

}
