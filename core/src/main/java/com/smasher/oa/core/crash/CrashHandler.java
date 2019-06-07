package com.smasher.oa.core.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.smasher.oa.core.config.Path;
import com.smasher.oa.core.io.FileUtil;
import com.smasher.oa.core.log.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;


/**
 * 异常处理
 * @author matao
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    private static final String MEMORY = "Memory";


    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }


    /**
     * 当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        try {
            handleException(ex);
        } catch (Exception e) {
            Logger.exception(e);
            e.printStackTrace();
        }

        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }


    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集设备参数信息
        // 保存日志文件
        String fileName = saveCrashInfoToFile(ex);
        return true;
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private String saveCrashInfoToFile(Throwable ex) {
        Properties deviceCrashInfo = collectCrashDeviceInfo();

        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        Logger.exception(ex);
        ex.printStackTrace();
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

        StringBuffer sb = new StringBuffer();
        for (Object key : deviceCrashInfo.keySet()) {
            sb.append(key.toString() + ":" + deviceCrashInfo.get(key) + "\r\n");
        }

        File file = new File(Path.getLogPath() + "crash_" + System.currentTimeMillis());
        FileUtil.createFile(file, true);
        FileUtil.saveFile(file, sb.toString());
        return file.getAbsolutePath();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    private Properties collectCrashDeviceInfo() {
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
}
