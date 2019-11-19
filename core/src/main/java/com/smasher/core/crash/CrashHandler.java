package com.smasher.core.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

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
 * 异常处理
 * @author matao
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private CrashHelper mHelper;

    private static final CrashHandler S_INSTANCE = new CrashHandler();

    private CrashHandler() {
        mHelper = new CrashHelper();
    }

    public static CrashHandler getInstance() {
        return S_INSTANCE;
    }


    /**
     * 这里主要完成初始化工作
     */
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

        mContext = context.getApplicationContext();
    }

    /**
     * 当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            boolean isCatch = handleException(ex);
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
        String fileName = mHelper.saveCrashInfoToFile(mContext, ex);
        return !TextUtils.isEmpty(fileName);
    }
}
