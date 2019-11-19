package com.smasher.core.crash;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.smasher.core.config.Path;
import com.smasher.core.io.FileUtil;
import com.smasher.core.log.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 崩溃处理
 * Created by huangzhaoyi on 2016/8/24.
 */
public class CrashManager {
    private static final String TAG = "CrashManager";

    //Extras passed to the error activity
    private static final String EXTRA_RESTART_ACTIVITY_CLASS = "EXTRA_RESTART_ACTIVITY_CLASS";
    private static final String EXTRA_SHOW_ERROR_DETAILS = "EXTRA_SHOW_ERROR_DETAILS";
    private static final String EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE";
    private static final String EXTRA_EVENT_LISTENER = "EXTRA_EVENT_LISTENER";
    //General constants
    private static final String INTENT_ACTION_ERROR_ACTIVITY = "com.xcm.oa.crash.ERROR";
    private static final String INTENT_ACTION_RESTART_ACTIVITY = "com.xcm.oa.crash.RESTART";
    private static final String CAOC_HANDLER_PACKAGE_NAME = "com.xcm.oa.crash";

    //128 KB - 1
    private static final int MAX_STACK_TRACE_SIZE = 131071;
    private static final int TIMESTAMP_DIFFERENCE_TO_AVOID_RESTART_LOOPS_IN_MILLIS = 2000;
    //Shared preferences
    private static final String SHARED_PREFERENCES_FILE = "custom_activity_on_crash";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";

    //Internal variables
    private static Application application;
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<>(null);
    private static boolean isInBackground = false;

    //Settable properties and their defaults
    private static boolean launchErrorActivityWhenInBackground = true;
    private static boolean showErrorDetails = true;
    private static boolean enableAppRestart = true;

    private static Class<? extends Activity> errorActivityClass = null;
    private static Class<? extends Activity> restartActivityClass = null;
    private static EventListener eventListener = null;

    /**
     * 安装
     *
     * @param context context
     */
    public static void install(Context context, boolean isDebug) {
        try {
            if (context == null) {
                return;
            }

            // 系统默认的异常处理handler
            final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (defaultHandler != null && defaultHandler.getClass().getName().startsWith(CAOC_HANDLER_PACKAGE_NAME)) {
                Logger.e(TAG, "You have already installed CustomActivityOnCrash, doing nothing!");
            } else {
                application = (Application) context.getApplicationContext();
                Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> resolveException(thread, throwable, defaultHandler));
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "An unknown error occurred while installing CustomActivityOnCrash, it may not have been properly initialized. Please report this as a bug if needed.", throwable);
        }
    }

    /**
     * 处理逻辑
     *
     * @param thread         thread
     * @param throwable      throwable
     * @param defaultHandler defaultHandler
     */
    private static void resolveException(Thread thread, Throwable throwable, Thread.UncaughtExceptionHandler defaultHandler) {
        // 检查最后Crash时间不超过2秒直接不处理
        if (hasCrashedInTheLastSeconds(application)) {
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
                return;
            }
        } else {
            setLastCrashTimestamp(application, System.currentTimeMillis());
            // 获取错误处理的activity
            if (errorActivityClass == null) {
                errorActivityClass = guessErrorActivityClass(application);
            }

            // 检查错误处理的activity是否crash了
            if (isStackTraceLikelyConflictive(throwable, errorActivityClass)) {
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, throwable);
                    return;
                }
            } else if (launchErrorActivityWhenInBackground || !isInBackground) {
                final Intent intent = new Intent(application, errorActivityClass);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String stackTraceString = sw.toString();

                if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                    String disclaimer = " [stack trace too large]";
                    stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
                }

                // 检查重启的activity是否存在
                if (enableAppRestart && restartActivityClass == null) {
                    restartActivityClass = guessRestartActivityClass(application);
                } else if (!enableAppRestart) {
                    restartActivityClass = null;
                }

                intent.putExtra(EXTRA_STACK_TRACE, stackTraceString);
                intent.putExtra(EXTRA_RESTART_ACTIVITY_CLASS, restartActivityClass);
                intent.putExtra(EXTRA_SHOW_ERROR_DETAILS, showErrorDetails);
                intent.putExtra(EXTRA_EVENT_LISTENER, eventListener);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (eventListener != null) {
                    eventListener.onLaunchErrorActivity();
                }
                application.startActivity(intent);

                // 处理异常记录日志
                handleException(throwable);
            }
        }
        // 关闭发生crash的activity
        final Activity lastActivity = lastActivityCreated.get();
        if (lastActivity != null) {
            lastActivity.finish();
            lastActivityCreated.clear();
        }
        killCurrentProcess();
    }

    /**
     * 异常处理
     *
     * @param ex ex
     * @return boolean
     */
    private static boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集设备参数信息
        // 保存日志文件
        CrashHelper helper = new CrashHelper();
        String fileName = helper.saveCrashInfoToFile(application, ex);
        return !TextUtils.isEmpty(fileName);
    }

    /**
     * 检查最后Crash时间
     *
     * @param context context
     * @return boolean
     */
    private static boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = System.currentTimeMillis();
        return (lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < TIMESTAMP_DIFFERENCE_TO_AVOID_RESTART_LOOPS_IN_MILLIS);
    }

    private static long getLastCrashTimestamp(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, -1);
    }

    private static void setLastCrashTimestamp(Context context, long timestamp) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).apply();
    }

    /**
     * 获取错误处理的activity
     *
     * @param context context
     * @return Class
     */
    private static Class<? extends Activity> guessErrorActivityClass(Context context) {
        Class<? extends Activity> resolvedActivityClass;

        //If action is defined, use that
        resolvedActivityClass = getErrorActivityClassWithIntentFilter(context);

        //Else, get the default launcher activity
        if (resolvedActivityClass == null) {
//            resolvedActivityClass = DefaultErrorActivity.class;
        }

        return resolvedActivityClass;
    }

    /**
     * 获取错误处理的activity
     *
     * @param context context
     * @return Class
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Activity> getErrorActivityClassWithIntentFilter(Context context) {
        Intent searchedIntent = new Intent().setAction(INTENT_ACTION_ERROR_ACTIVITY).setPackage(context.getPackageName());
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(searchedIntent, PackageManager.GET_RESOLVED_FILTER);

        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e(TAG, "Failed when resolving the error activity class via intent filter, stack trace follows!", e);
            }
        }
        return null;
    }

    /**
     * 获取重新启动的activity
     *
     * @param context context
     * @return Class
     */
    private static Class<? extends Activity> guessRestartActivityClass(Context context) {
        Class<? extends Activity> resolvedActivityClass;

        //If action is defined, use that
        resolvedActivityClass = getRestartActivityClassWithIntentFilter(context);

        //Else, get the default launcher activity
        if (resolvedActivityClass == null) {
            resolvedActivityClass = getLauncherActivity(context);
        }

        return resolvedActivityClass;
    }

    /**
     * 从intent的action里获取重新启动的activity
     *
     * @param context context
     * @return Class
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Activity> getRestartActivityClassWithIntentFilter(Context context) {
        Intent searchedIntent = new Intent().setAction(INTENT_ACTION_RESTART_ACTIVITY).setPackage(context.getPackageName());
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(searchedIntent, PackageManager.GET_RESOLVED_FILTER);

        if (resolveInfos != null && resolveInfos.size() > 0) {
            ResolveInfo resolveInfo = resolveInfos.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e(TAG, "Failed when resolving the restart activity class via intent filter, stack trace follows!", e);
            }
        }
        return null;
    }

    /**
     * 从intent里获取重新启动的activity
     *
     * @param intent intent
     * @return Class
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Activity> getRestartActivityClassFromIntent(Intent intent) {
        Serializable serializedClass = intent.getSerializableExtra(EXTRA_RESTART_ACTIVITY_CLASS);
        if (serializedClass instanceof Class) {
            return (Class<? extends Activity>) serializedClass;
        } else {
            return null;
        }
    }

    /**
     * 获取启动activity
     *
     * @param context context
     * @return Class
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Activity> getLauncherActivity(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            try {
                ComponentName componentName = intent.getComponent();
                String name = "";
                if (componentName != null) {
                    name = componentName.getClassName();
                }
                if (!TextUtils.isEmpty(name)) {
                    return (Class<? extends Activity>) Class.forName(name);
                }

            } catch (ClassNotFoundException e) {
                //Should not happen, print it to the log!
                Log.e(TAG, "Failed when resolving the restart activity class via getLaunchIntentForPackage, stack trace follows!", e);
            }
        }

        return null;
    }

    private static boolean isStackTraceLikelyConflictive(Throwable throwable, Class<? extends Activity> activityClass) {
        do {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                boolean equs = "android.app.ActivityThread".equals(element.getClassName());
                boolean eque = "handleBindApplication".equals(element.getMethodName());
                boolean equf = element.getClassName().equals(activityClass.getName());
                boolean condition = (equs && eque) || equf;
                if (condition) {
                    return true;
                }
            }
        } while ((throwable = throwable.getCause()) != null);
        return false;
    }

    /**
     * 重新启动activity
     *
     * @param activity      activity
     * @param intent        intent
     * @param eventListener eventListener
     */
    public static void restartApplicationWithIntent(Activity activity, Intent intent, EventListener eventListener) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (eventListener != null) {
            eventListener.onRestartAppFromErrorActivity();
        }
        activity.finish();
        activity.startActivity(intent);
        killCurrentProcess();
    }

    /**
     * 关闭app
     *
     * @param activity      activity
     * @param eventListener eventListener
     */
    public static void closeApplication(Activity activity, EventListener eventListener) {
        if (eventListener != null) {
            eventListener.onCloseAppFromErrorActivity();
        }
        activity.finish();
        killCurrentProcess();
    }

    /**
     * 杀掉当前进程
     */
    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    public static void setErrorActivityClass(Class<? extends Activity> aClass) {
        errorActivityClass = aClass;
    }

    public static void setRestartActivityClass(Class<? extends Activity> aClass) {
        restartActivityClass = aClass;
    }


    /**
     * Activity事件回调
     */
    public interface EventListener extends Serializable {
        void onLaunchErrorActivity();

        void onRestartAppFromErrorActivity();

        void onCloseAppFromErrorActivity();
    }
}
