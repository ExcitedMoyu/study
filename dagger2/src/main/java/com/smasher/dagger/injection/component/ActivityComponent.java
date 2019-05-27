package com.smasher.dagger.injection.component;

import com.smasher.dagger.activity.AlarmActivity;
import com.smasher.dagger.activity.DaggerActivity;
import com.smasher.dagger.injection.module.ActivityModule;
import com.smasher.dagger.injection.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * 注入器
 *
 * @author matao
 * @date 2019/5/15
 */
@ActivityScope
@Subcomponent( modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(AlarmActivity alarmActivity);

    void inject(DaggerActivity factoryActivity);

}
