package com.smasher.rejuvenation.entity

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author moyu
 */
class ActivityManager private constructor() {

    init {
        activityStack = Stack()
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return if (!activityStack.isEmpty()) {
            activityStack.lastElement()
        } else null
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        if (!activityStack.isEmpty()) {
            val activity = activityStack.lastElement()
            finishActivity(activity)
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i].finish()
            }
            i++
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     */
    fun appexit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.restartPackage(context.packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private var instance: ActivityManager? = null
            get() {
                if (field == null) {
                    field = ActivityManager()
                }
                return field
            }

        @Synchronized
        fun get(): ActivityManager {
            return instance!!
        }

        private lateinit var activityStack: Stack<Activity>
    }
}
