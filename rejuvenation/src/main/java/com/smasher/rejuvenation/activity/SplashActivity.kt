package com.smasher.rejuvenation.activity

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import com.smasher.ndk.PrimaryService
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.util.LogUtil
import com.smasher.widget.base.BaseActivity

/**
 * @author matao
 * @date 2019/6/11
 */
class SplashActivity :  BaseActivity(), Handler.Callback{


    var a = 0
    private var mHandler: Handler? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        val mWindow = this.window
        mWindow.setFormat(PixelFormat.OPAQUE)

        val uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)


        val decorView = mWindow.decorView
        decorView.systemUiVisibility = uiOptions

        super.onCreate(savedInstanceState)



        LogUtil.d("onCreate:")
        mHandler = Handler(this)
    }

    override fun setFunctionsForFragment(tag: String?) {
    }

    override fun getRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_splash, null);
    }

    override fun initView() {
    }

    override fun initData() {
        startPrimaryService()
    }

    override fun onResume() {
        super.onResume()
        mHandler!!.postDelayed({
            next()
            finish()
        }, 3000)

    }

    private fun next() {
        val intent: Intent = Intent()
        intent.setClass(this, MajorActivity::class.java)
        startActivity(intent)
    }


    private fun startPrimaryService() {
        val intent = Intent()
        intent.setClass(this, PrimaryService::class.java)
        startService(intent)
    }


    override fun handleMessage(msg: Message?): Boolean {
        val id: Int = msg!!.what
        a = when (id) {
            0 -> 0
            1 -> 1
            2 -> 3
            else -> -1
        }
        return false
    }

}