package com.smasher.rejuvenation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.smasher.aidl.activity.local.LocalActivity
import com.smasher.aidl.activity.remote.ui.login.LoginActivity
import com.smasher.draw.activity.DrawableActivity
import com.smasher.media.activity.TestActivity
import com.smasher.oa.core.utils.StatusBarUtil
import com.smasher.rejuvenation.R
import com.smasher.widget.activity.AlarmActivity
import com.smasher.widget.activity.WidgetActivity
import com.smasher.widget.base.BaseActivity
import kotlinx.android.synthetic.main.activity_basics.*

class BasicActivity : BaseActivity(), View.OnClickListener {


    private var mButton1: Button? = null
    private var mButton2: Button? = null
    private var mButton3: Button? = null
    private var mButton4: Button? = null
    private var mButton5: Button? = null
    private var mButton6: Button? = null
    private var mButton7: Button? = null
    private var mButton8: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucent(this)
        toolbar.title = "BasicActivity"
        setSupportActionBar(toolbar)
        initListener()
    }


    override fun getRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_basics, null)
    }

    override fun initView() {
        mButton1 = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
        mButton5 = findViewById(R.id.button5)
        mButton6 = findViewById(R.id.button6)
        mButton7 = findViewById(R.id.button7)
        mButton8 = findViewById(R.id.button8)
    }


    override fun setFunctionsForFragment(tag: String?) {

    }

    override fun initData() {

    }


    private fun initListener() {
        mButton1!!.setOnClickListener(this)
        mButton2!!.setOnClickListener(this)
        mButton3!!.setOnClickListener(this)
        mButton4!!.setOnClickListener(this)
        mButton5!!.setOnClickListener(this)
        mButton6!!.setOnClickListener(this)
        mButton7!!.setOnClickListener(this)
        mButton8!!.setOnClickListener(this)
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
            R.id.button7 -> Log.d("TAG", "button")
            R.id.button8 -> Log.d("TAG", "button")
            else -> Log.d("TAG", "button")
        }
    }

    private fun gotoLogin() {
        val intent = Intent()
        intent.setClass(this, LoginActivity::class.java)
        startActivity(intent)
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
        val intent = Intent()
        intent.setClass(this, LocalActivity::class.java)
        startActivity(intent)
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

}
