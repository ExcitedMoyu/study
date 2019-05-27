package com.smasher.dagger.injection.module;

import com.smasher.dagger.entity.SellMoe;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author matao
 * @date 2019/5/27
 */
@Module
public class OkHttpModule {

    @Singleton
    @Provides
    public OkHttpClient providerOkHttpClient() {
        return new OkHttpClient();
    }

}
