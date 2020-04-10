package com.smasher.rejuvenation.base;

import android.view.View;


import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author matao
 */
public abstract class BaseFragment extends com.smasher.widget.base.BaseFragment {

    private Unbinder unbinder;


    @Override
    protected void initButterKnife(View view) {
        super.initButterKnife(view);
        unbinder = ButterKnife.bind(this, view);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
