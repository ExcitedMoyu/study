//package com.smasher.media
//
//import android.content.Context
//import androidx.multidex.MultiDexApplication
//import com.smasher.zxing.activity.ZXingLibrary
//import leakcanary.LeakCanary
//import leakcanary.LeakSentry
//
///**
// * @author matao
// * @date 2019/6/10
// */
//class SmasherApplication : MultiDexApplication() {
//
//
//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//    }
//
//
//    override fun onCreate() {
//        super.onCreate()
//
//        LeakSentry.config = LeakSentry.config.copy(watchFragments = false)
//    }
//}