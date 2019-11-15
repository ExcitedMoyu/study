package com.smasher.rejuvenation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
//import com.smasher.aidl.activity.local.LocalActivity
//import com.smasher.aidl.activity.remote.ui.login.LoginActivity
import com.smasher.draw.activity.DrawableActivity
import com.smasher.media.activity.TestActivity
import com.smasher.core.utils.StatusBarUtil
import com.smasher.rejuvenation.R
import com.smasher.widget.activity.AlarmActivity
import com.smasher.widget.activity.WidgetActivity
import com.smasher.widget.base.BaseActivity
import com.smasher.widget.basic.IndicatorActivity
import com.smasher.widget.slideswaphelper.test.SlideMenuActivity
import kotlinx.android.synthetic.main.activity_practise.*
import kotlinx.android.synthetic.main.content_basic.*

class PractiseActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucent(this)
        toolbar.title = "PractiseActivity"
        setSupportActionBar(toolbar)
        initListener()
    }


    override fun getRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_practise, null)
    }

    override fun initView() {

    }

    override fun setFunctionsForFragment(tag: String?) {

    }

    override fun initData() {

    }


    private fun initListener() {
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button1 -> gotoLogin()
            R.id.button2 -> gotoAlarm()
            R.id.button3 -> gotoDrawable()
            R.id.button4 -> gotoLocal()
            R.id.button5 -> gotoWidget()
            R.id.button6 -> gotoTest()
            R.id.button7 -> gotoIndicator()
            R.id.button8 -> gotoSlide()
            else -> Log.d("TAG", "button")
        }
    }

    private fun gotoIndicator() {
        val intent = Intent()
        intent.setClass(this, IndicatorActivity::class.java)
        startActivity(intent)
    }

    private fun gotoLogin() {
//        val intent = Intent()
//        intent.setClass(this, LoginActivity::class.java)
//        startActivity(intent)
    }

    private fun gotoAlarm() {
        val intent = Intent()
        intent.setClass(this, AlarmActivity::class.java)
        startActivity(intent)
    }

    private fun gotoDrawable() {
        val intent = Intent()
        intent.setClass(this, DrawableActivity::class.java)
        startActivity(intent)
    }

    private fun gotoLocal() {
//        val intent = Intent()
//        intent.setClass(this, LocalActivity::class.java)
//        startActivity(intent)
    }

    private fun gotoWidget() {
        val intent = Intent()
        intent.setClass(this, WidgetActivity::class.java)
        startActivity(intent)
    }

    private fun gotoTest() {
        val intent = Intent()
        intent.setClass(this, TestActivity::class.java)
        startActivity(intent)
    }

    private fun gotoSlide() {
        val intent = Intent()
        intent.setClass(this, SlideMenuActivity::class.java)
        startActivity(intent)
    }
}
