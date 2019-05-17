package com.smasher.study.interfaces;

import com.smasher.study.FactoryActivity;

import dagger.Component;

/**
 * @author matao
 * @date 2019/5/15
 */
@Component
public interface FactoryActivityComponent {

    void inject(FactoryActivity factoryActivity);

}
