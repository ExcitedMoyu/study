package com.smasher.dagger.injection.module;

import com.smasher.dagger.entity.HelloWorld;

import dagger.Module;
import dagger.Provides;

/**
 * @author matao
 * @date 2019/5/27
 */
@Module
public class ActivityModule {


    @Provides
    HelloWorld providerSellMoe() {
        return new HelloWorld();
    }
}
