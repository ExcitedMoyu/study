package com.smasher.kotlin

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * @author matao
 * @date 2019/5/23
 */
class StudyApplication : Application() {

    private val TAG: String = "StudyApplication"

    init {
        Log.d(TAG, "StudyApplication init")
    }


    override fun onCreate() {
        super.onCreate()
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun onLowMemory() {
        super.onLowMemory()
    }

}
