package com.smasher.core.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.smasher.core.config.Path;
import com.smasher.core.io.FileUtil;
import com.smasher.core.log.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * @author Smasher
 * on 2019/11/19 0019
 */
public class CrashHelper {
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    private static final String MEMORY = "Memory";

    public CrashHelper() {
    }

    /**
     * 收集程序崩溃的设备信息
     */
    private Properties collectCrashDeviceInfo(Context mContext) {
        Properties deviceCrashInfo = new Properties();
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                deviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                deviceCrashInfo.put(VERSION_CODE, String.valueOf(pi.versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.exception(e);
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 具体信息请参考后面的截图
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceCrashInfo.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Logger.exception(e);
            }

        }
        return deviceCrashInfo;
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex ex
     * @return String
     */
    public String saveCrashInfoToFile(Context context, Throwable ex) {
        Properties deviceCrashInfo = collectCrashDeviceInfo(context);

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        Logger.exception(ex);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        Date date = new Date(System.currentTimeMillis());
        String result = date.toString() + info.toString();
        printWriter.close();
        deviceCrashInfo.put(STACK_TRACE, result);
        deviceCrashInfo.put(MEMORY, "");

        StringBuffer stringBuffer = new StringBuffer();
        for (Object key : deviceCrashInfo.keySet()) {
            stringBuffer.append(key.toString()).append(":").append(deviceCrashInfo.get(key)).append("\r\n");
        }

        File file = new File(Path.getLogPath() + "crash_" + System.currentTimeMillis());
        FileUtil.createFile(file, true);
        FileUtil.saveFile(file, stringBuffer.toString());
        return file.getAbsolutePath();
    }

}
