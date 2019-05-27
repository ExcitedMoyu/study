package com.smasher.dagger.injection.component;

import com.smasher.dagger.DaggerApplication;
import com.smasher.dagger.injection.module.OkHttpModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author matao
 * @date 2019/5/27
 */
@Singleton
@Component(modules = OkHttpModule.class)
public interface OkHttpComponent {

    /**
     * 注入application  全局单例
     *
     * @param application application
     */
    void inject(DaggerApplication application);

    /**
     * 说明将此component给其他Component使用
     */
    OkHttpClient getOkHttpClient();


    ActivityComponent getActivityComponent();
}
