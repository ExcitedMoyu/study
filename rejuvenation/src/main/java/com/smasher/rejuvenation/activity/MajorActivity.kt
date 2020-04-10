package com.smasher.rejuvenation.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.smasher.media.activity.InitActivity
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.adapter.MajorAdapter
import com.smasher.rejuvenation.entity.MajorData
import com.smasher.rxjava.RxJavaActivity
import com.smasher.rejuvenation.base.BaseActivity
import com.smasher.zxing.activity.CaptureActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class MajorActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {


    private var mList: MutableList<MajorData>? = null
    private var mAdapter: MajorAdapter? = null
    private var mRecyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        initDrawer()
    }

    private fun initListener() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView)
        mAdapter = MajorAdapter(this)
        mRecyclerView!!.adapter = mAdapter
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
    }

    private fun initDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun initData() {
        buildList()
    }

    override fun getRootViewRes(): Int {
        return R.layout.activity_major
    }

    private fun buildList() {
        mList = mutableListOf()
        for (index in 1..30) {
            val item = MajorData(index)
            mList!!.add(item)
        }
        mAdapter!!.setData(mList)
        mAdapter!!.notifyDataSetChanged()
        Log.d(TAG, "buildList")
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                gotoScan()
            }
            R.id.nav_gallery -> {
                gotoDagger()
            }
            R.id.nav_slideshow -> {
                gotoPractise()
            }
            R.id.nav_tools -> {
                gotoMedia()
            }
            R.id.nav_share -> {
                gotoRxJava()
            }
            R.id.nav_send -> {
                gotoDebug()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private fun checkCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    fun gotoScan() {
        val hasPermission = checkCameraPermission()
        if (hasPermission) {
            val intent = Intent()
            intent.setClass(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SCAN)
        } else {
            val builder = PermissionRequest.Builder(this, REQUEST_CODE_PERMISSION, Manifest.permission.CAMERA)
            builder.setNegativeButtonText("cancel")
            builder.setPositiveButtonText("ok")
            builder.setRationale("need permission")
            val request = builder.build()
            EasyPermissions.requestPermissions(request)
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsDenied")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted")
    }


    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        Log.d(TAG, "shouldShowRequestPermissionRationale")
        return super.shouldShowRequestPermissionRationale(permission)
    }


    private fun gotoDebug() {
        val intent = Intent()
        intent.setClass(this, DebugActivity::class.java)
        startActivity(intent)
    }

    private fun gotoDagger() {
        val intent = Intent()
        intent.setClass(this, DaggerActivity::class.java)
        startActivity(intent)
    }

    private fun gotoMedia() {
        val intent = Intent()
        intent.setClass(this, InitActivity::class.java)
        startActivity(intent)
    }


    private fun gotoRxJava() {
        val intent = Intent()
        intent.setClass(this, RxJavaActivity::class.java)
        startActivity(intent)
    }

    private fun gotoPractise() {
        val intent = Intent()
        intent.setClass(this, PractiseActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_CODE_SCAN = 999
        private const val REQUEST_CODE_PERMISSION = 1000
        private const val TAG = "MajorActivity"
    }
}
