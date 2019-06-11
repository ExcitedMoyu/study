package com.smasher.kotlin

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * @author matao
 * @date 2019/5/23
 */
class  StudyApplication : Application() {

    init {
        Log.d(Companion.TAG, "StudyApplication init")
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

    companion object {
        private const val TAG: String = "StudyApplication"
    }

}
