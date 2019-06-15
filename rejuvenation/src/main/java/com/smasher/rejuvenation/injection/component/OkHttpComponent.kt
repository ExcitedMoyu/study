package com.smasher.rejuvenation.injection.component

import com.smasher.dagger.injection.module.OkHttpModule
import com.smasher.rejuvenation.RejuvenatedApplication
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * @author matao
 * @date 2019/5/27
 */
@Singleton
@Component(modules = [OkHttpModule::class])
interface OkHttpComponent {

    /**
     * 说明将此component给其他Component使用
     */
    val okHttpClient: OkHttpClient


    val activityComponent: ActivityComponent

    /**
     * 注入application  全局单例
     *
     * @param application application
     */
    fun inject(application: RejuvenatedApplication)
}
