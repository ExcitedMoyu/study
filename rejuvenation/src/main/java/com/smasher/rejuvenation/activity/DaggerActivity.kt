package com.smasher.rejuvenation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.smasher.dagger.entity.HelloWorld
import com.smasher.dagger.entity.SellMoe
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.RejuvenatedApplication
import com.smasher.widget.base.BaseActivity
import javax.inject.Inject


/**
 * @author matao
 */
class DaggerActivity : BaseActivity(), View.OnClickListener {

    private var hello: Button? = null

    @Inject
    @JvmField
    var mHelloWorld: HelloWorld? = null

    @Inject
    @JvmField
    var mProduct: SellMoe? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application: RejuvenatedApplication = application as RejuvenatedApplication
        val okHttpComponent = application.getOkHttpComponent()
        val activityComponent = okHttpComponent.activityComponent
        activityComponent.inject(this)

        mHelloWorld!!.sayHello(this)
        mProduct!!.name = "測試"

    }


    override fun initData() {
    }

    override fun getRootViewRes(): Int {
        return R.layout.activity_dagger
    }


    override fun initView() {
        hello = findViewById(R.id.hello)
        hello?.setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.activity_description_dagger)
    }


    override fun onResume() {
        super.onResume()

        try {
            Log.d(TAG, "onResume: " + mProduct!!.name);
            Log.d(TAG, "onResume: mHelloWorld:" + mHelloWorld!!.sellMoe.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View) {
        Toast.makeText(this, "測試", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "DaggerActivity"
    }
}
