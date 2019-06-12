package com.smasher.rejuvenation

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.smasher.oa.core.crash.CrashHandler
import com.smasher.oa.core.crash.CrashManager
import com.smasher.oa.core.other.ApplicationContext
import com.smasher.rejuvenation.activity.CrashActivity
import com.smasher.rejuvenation.activity.SplashActivity
import com.smasher.rejuvenation.injection.component.DaggerOkHttpComponent
import com.smasher.rejuvenation.injection.component.OkHttpComponent
import com.smasher.zxing.activity.ZXingLibrary

/**
 * @author matao
 * @date 2019/6/11
 */
class RejuvenatedApplication : MultiDexApplication() {

    var mPackageNema: String = ""
    var sContext: Context? = null

    internal lateinit var mOkHttpComponent: OkHttpComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mPackageNema = packageName
        ApplicationContext.setApplicationContext(this)
    }

    override fun onCreate() {
        super.onCreate()
        sContext = applicationContext
        mOkHttpComponent = DaggerOkHttpComponent.create()
        mOkHttpComponent.inject(this)
        ZXingLibrary.initDisplayOpinion(this)
        initCrash(false)
    }

    private fun initCrash(isDebug: Boolean) {
        if (isDebug) {
            CrashHandler.getInstance().init(sContext)
        } else {
            CrashManager.setErrorActivityClass(CrashActivity::class.java)
            CrashManager.setRestartActivityClass(SplashActivity::class.java)
            CrashManager.install(sContext, isDebug)
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }


    fun getOkHttpComponent(): OkHttpComponent {
        return mOkHttpComponent
    }
}