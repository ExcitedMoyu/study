package com.smasher.dagger;

import android.app.Application;
import android.content.Context;

import com.smasher.dagger.injection.component.DaggerOkHttpComponent;
import com.smasher.dagger.injection.component.OkHttpComponent;

/**
 * @author matao
 * @date 2019/5/27
 */
public class DaggerApplication extends Application {
    OkHttpComponent mOkHttpComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mOkHttpComponent = DaggerOkHttpComponent.create();
        mOkHttpComponent.inject(this);
    }


    public OkHttpComponent getOkHttpComponent() {
        return mOkHttpComponent;
    }
}
