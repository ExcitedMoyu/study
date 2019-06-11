package com.smasher.rejuvenation.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.smasher.aidl.activity.local.LocalActivity
import com.smasher.aidl.activity.remote.ui.login.LoginActivity
import com.smasher.draw.activity.DrawableActivity
import com.smasher.media.activity.TestActivity
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.util.LogUtil
import com.smasher.widget.alarm.AlarmActivity
import com.smasher.widget.alarm.WidgetActivity
import com.smasher.widget.base.BaseActivity
import com.smasher.widget.basic.BasicActivity
import com.smasher.zxing.activity.CaptureActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DebugActivity : BaseActivity(), View.OnClickListener {


    private val mHideHandler = Handler()
    private var mButton: Button? = null
    private var mButton2: Button? = null
    private var mButton3: Button? = null
    private var mButton4: Button? = null
    private var mButton5: Button? = null
    private var mButton6: Button? = null
    private var mButton7: Button? = null
    private var mButton8: Button? = null
    private var mDummyButton: Button? = null

    private var mContentView: View? = null
    private var mControlsView: View? = null
    private var mVisible: Boolean = false


    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        mContentView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
        mControlsView!!.visibility = View.VISIBLE
    }


    private val mHideRunnable = { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    @SuppressLint("ClickableViewAccessibility")
    private val mDelayHideTouchListener: View.OnTouchListener = View.OnTouchListener { v, event ->
        LogUtil.d(v.toString())
        LogUtil.d(event.action.toString())
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
        initStatus()
    }


    override fun setFunctionsForFragment(tag: String?) {

    }

    override fun getRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_debug, null)
    }

    override fun initData() {

    }


    private fun initStatus() {
        mVisible = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        mButton!!.setOnClickListener(this)
        mButton2!!.setOnClickListener(this)
        mButton3!!.setOnClickListener(this)
        mButton4!!.setOnClickListener(this)
        mButton5!!.setOnClickListener(this)
        mButton6!!.setOnClickListener(this)
        mButton7!!.setOnClickListener(this)
        mButton8!!.setOnClickListener(this)
        mDummyButton!!.setOnTouchListener(mDelayHideTouchListener)
        mContentView!!.setOnClickListener { view -> toggle() }
    }

    override fun initView() {
        mButton = findViewById(R.id.button)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
        mButton5 = findViewById(R.id.button5)
        mButton6 = findViewById(R.id.button6)
        mButton7 = findViewById(R.id.button7)
        mButton8 = findViewById(R.id.button8)
        mContentView = findViewById(R.id.fullscreen_content)
        mControlsView = findViewById(R.id.fullscreen_content_controls)
        mDummyButton = findViewById(R.id.dummy_button)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        mControlsView!!.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        // Show the system bar
        mContentView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun checkCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button) {
            onButtonClicked()
        } else if (id == R.id.button2) {
            onMButton2Clicked()
        } else if (id == R.id.button3) {
            onMButton3Clicked()
        } else if (id == R.id.button4) {
            onMButton4Clicked()
        } else if (id == R.id.button5) {
            onMButton5Clicked()
        } else if (id == R.id.button6) {
            onMButton6Clicked()
        } else if (id == R.id.button7) {
            onMButton7Clicked()
        } else if (id == R.id.button8) {
            onMButton8Clicked()
        }
    }


    fun onButtonClicked() {
        val intent = Intent()
        intent.setClass(this, BasicActivity::class.java)
        startActivity(intent)
    }

    fun onMButton2Clicked() {
        val intent = Intent()
        intent.setClass(this, AlarmActivity::class.java)
        startActivity(intent)
    }

    fun onMButton3Clicked() {
        val intent = Intent()
        intent.setClass(this, DrawableActivity::class.java)
        startActivity(intent)
    }

    fun onMButton4Clicked() {
        val intent = Intent()
        intent.setClass(this, LocalActivity::class.java)
        startActivity(intent)
    }

    fun onMButton5Clicked() {
        val intent = Intent()
        intent.setClass(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    fun onMButton6Clicked() {
        val hasPermission = checkCameraPermission()
        if (hasPermission) {
            val intent = Intent()
            intent.setClass(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PERMISSION)
        } else {
            val builder = PermissionRequest.Builder(this, REQUEST_CODE_PERMISSION, Manifest.permission.CAMERA)
            val request = builder.build()
            EasyPermissions.requestPermissions(request)
        }


    }

    fun onMButton7Clicked() {
        val intent = Intent()
        intent.setClass(this, WidgetActivity::class.java)
        startActivity(intent)
    }

    fun onMButton8Clicked() {
        val intent = Intent()
        intent.setClass(this, TestActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [.AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [.AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
        private val REQUEST_CODE_SCAN = 999
        const val REQUEST_CODE_PERMISSION = 1000
    }

}
