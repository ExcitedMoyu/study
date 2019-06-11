package com.smasher.rejuvenation.injection.component

import com.smasher.dagger.injection.module.ActivityModule
import com.smasher.dagger.injection.scope.ActivityScope
import com.smasher.rejuvenation.activity.DaggerActivity

import dagger.Subcomponent

/**
 * 注入器
 *
 * @author matao
 * @date 2019/5/15
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(factoryActivity: DaggerActivity)

}
