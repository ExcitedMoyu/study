package com.smasher.media;

import androidx.multidex.MultiDexApplication;

import com.smasher.zxing.activity.ZXingLibrary;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author matao
 * @date 2019/6/10
 */
public class SmasherApplication extends MultiDexApplication {

    private static RefWatcher sRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        sRefWatcher = LeakCanary.install(this);
    }


    public static RefWatcher getRefWatcher() {
        return sRefWatcher;
    }
}
