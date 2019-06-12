package com.smasher.rejuvenation.activity

import android.view.LayoutInflater
import android.view.View
import com.smasher.rejuvenation.R
import com.smasher.widget.base.BaseActivity

class CrashActivity : BaseActivity() {
    override fun setFunctionsForFragment(tag: String?) {

    }

    override fun getRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_dagger, null)
    }

    override fun initView() {

    }

    override fun initData() {

    }

}
