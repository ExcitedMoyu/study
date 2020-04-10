package com.smasher.rejuvenation.base;

import butterknife.ButterKnife;


/**
 * @author matao
 */
public abstract class BaseActivity extends com.smasher.widget.base.BaseActivity {


    @Override
    protected void initButterKnife() {
        super.initButterKnife();
        //ButterKnife
        ButterKnife.bind(this);
    }
}
