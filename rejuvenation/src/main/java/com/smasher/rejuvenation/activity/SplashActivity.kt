package com.smasher.rejuvenation.activity

import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.smasher.core.utils.DensityUtil
import com.smasher.downloader.handler.WeakReferenceHandler
import com.smasher.draw.bean.CircleBean
import com.smasher.ndk.PrimaryService
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.util.LogUtil
import com.smasher.rejuvenation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * @author matao
 * @date 2019/6/11
 */
class SplashActivity : BaseActivity(), Handler.Callback {


    var a = 0
    private var mHandler: WeakReferenceHandler? = null
    private val circleBeanList = ArrayList<CircleBean>()
    private lateinit var future: ScheduledFuture<*>

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
        mHandler = WeakReferenceHandler(this)
    }


    override fun initView() {
        image.setImageResource(R.drawable.back_space)
        skip.text = getString(R.string.splash_skip, "3")
        skip.setOnClickListener {
            if (!future.isCancelled) {
                future.cancel(true)
            }
            next()
            finish()
        }
    }

    override fun initData() {
        startPrimaryService()
        initPoint()
        bubble!!.circleBeen = circleBeanList
    }

    override fun getRootViewRes(): Int {
        return R.layout.activity_splash
    }

    override fun onResume() {
        super.onResume()
        val scaleAnimation = ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.fillAfter = true
        scaleAnimation.interpolator = AccelerateDecelerateInterpolator()
        scaleAnimation.duration = 3000
        image!!.startAnimation(scaleAnimation)


        mHandler!!.postDelayed({
            bubble.openAnimation()
        }, 200)


        val schedule = Executors.newScheduledThreadPool(3)
        var i = 3
        future = schedule.scheduleAtFixedRate({
            Log.d(TAG, "splash开启中$i")
            i--

            val msg = Message.obtain()
            if (i == 0) {
                msg.what = 1
                msg.arg1 = i
            } else {
                msg.what = 0
                msg.arg1 = i
            }
            mHandler!!.sendMessage(msg)
        }, 1, 1, TimeUnit.SECONDS)
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


    override fun handleMessage(msg: Message): Boolean {
        val id: Int = msg.what
        val time: Int = msg.arg1
        skip!!.text = getString(R.string.splash_skip, time.toString())
        when (id) {
            0 -> {
                Log.d(TAG, "$time")
            }
            1 -> {
                if (!future.isCancelled) {
                    future.cancel(true)
                }
                next()
                finish()
            }
            else -> -1
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
    }


    private fun initPoint() {

        val width = DensityUtil.getScreenWidth(this)
        val height = DensityUtil.getScreenHeight(this)
        Log.d(TAG, "initPoint: $height----$width")
        val centerX = width / 2
        val centerY = height / 2
        Log.d(TAG, "initPoint: $centerX----$centerY")

        val circleBean = CircleBean(
                PointF((-width / 5.1).toFloat(), (height / 1.5).toFloat()),
                PointF((centerX - 30).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 2.4).toFloat(), (height / 3.4).toFloat()),
                PointF((width / 6).toFloat(), (centerY - 120).toFloat()),
                PointF((width / 7.2).toFloat(), (-height / 128).toFloat()),
                (width / 14.4).toFloat(), 60)
        val circleBean2 = CircleBean(
                PointF((-width / 4).toFloat(), (height / 1.3).toFloat()),
                PointF((centerX - 20).toFloat(), (height * 3 / 5).toFloat()),
                PointF((width / 2.1).toFloat(), (height / 2.5).toFloat()),
                PointF((width / 3).toFloat(), (centerY - 10).toFloat()),
                PointF((width / 4).toFloat(), (-height / 5.3).toFloat()),
                width / 4f, 60)
        val circleBean3 = CircleBean(
                PointF((-width / 12).toFloat(), (height / 1.1).toFloat()),
                PointF((centerX - 100).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 3.4).toFloat(), (height / 2).toFloat()),
                PointF(0f, (centerY + 100).toFloat()),
                PointF(0f, 0f),
                width / 24f, 60)

        val circleBean4 = CircleBean(
                PointF((-width / 9).toFloat(), (height / 0.9).toFloat()),
                PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
                PointF((width / 2.1).toFloat(), (height / 2.3).toFloat()),
                PointF((width / 2).toFloat(), centerY.toFloat()),
                PointF((width / 1.5).toFloat(), (-height / 5.6).toFloat()),
                width / 4f, 60)

        val circleBean5 = CircleBean(
                PointF((width / 1.4).toFloat(), (height / 0.9).toFloat()),
                PointF(centerX.toFloat(), (height * 3 / 4).toFloat()),
                PointF((width / 2).toFloat(), (height / 2.37).toFloat()),
                PointF((width * 10 / 13).toFloat(), (centerY - 20).toFloat()),
                PointF((width / 2).toFloat(), (-height / 7.1).toFloat()),
                width / 4f, 60)
        val circleBean6 = CircleBean(
                PointF((width / 0.8).toFloat(), height.toFloat()),
                PointF((centerX + 20).toFloat(), (height * 2 / 3).toFloat()),
                PointF((width / 1.9).toFloat(), (height / 2.3).toFloat()),
                PointF((width * 11 / 14).toFloat(), (centerY + 10).toFloat()),

                PointF((width / 1.1).toFloat(), (-height / 6.4).toFloat()),
                (width / 4).toFloat(), 60)
        val circleBean7 = CircleBean(
                PointF((width / 0.9).toFloat(), (height / 1.2).toFloat()),
                PointF((centerX + 20).toFloat(), (height * 4 / 7).toFloat()),
                PointF((width / 1.6).toFloat(), (height / 1.9).toFloat()),
                PointF(width.toFloat(), (centerY + 10).toFloat()),

                PointF(width.toFloat(), 0f),
                (width / 9.6).toFloat(), 60)

        circleBeanList.add(circleBean)
        circleBeanList.add(circleBean2)
        circleBeanList.add(circleBean3)
        circleBeanList.add(circleBean4)
        circleBeanList.add(circleBean5)
        circleBeanList.add(circleBean6)
        circleBeanList.add(circleBean7)

    }

    companion object {
        private const val TAG = "SplashActivity"
    }

}