package com.smasher.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smasher.kotlin.adapter.Adapter
import com.smasher.kotlin.R
import com.smasher.kotlin.entity.DDD

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var count: Int = 0

    var editWatcher1: EditWatcher = EditWatcher()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        study.text = getString(R.string.app_name)
        study.setOnClickListener(this@MainActivity)

        var editWatcher: EditWatcher = EditWatcher()
        editText.addTextChangedListener(editWatcher)
        editText.addTextChangedListener(editWatcher1)

        testObject()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        var mList: MutableList<DDD>? = mutableListOf()
        var mAdapter = Adapter(this)
        count++
        Log.d(TAG, count.toString())
        testFor(mList)
        mAdapter.setData(mList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    private fun testFor(mList: MutableList<DDD>?) {
        for (i in 0 until 100) {
            var item: DDD = DDD("", 26)
            mList!!.add(item)
        }
    }


    private fun testObject() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


    inner class EditWatcher : TextWatcher {
        init {
            Log.d(TAG, "EditWatcher init")
        }

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onClick(v: View?) {
        var id = v!!.id
        when (id) {
            R.id.study -> {

                //way1
                var intent1 = Intent()
                intent1.setClass(this@MainActivity, SubActivity::class.java)
                startActivity(intent1)

                //way2
                var intent2 = intentFor<SubActivity>(
                        "aaa" to "asd"
                )
                startActivity(intent2)


                //way3
                var bundle1 = Bundle()
                var intent3 = Intent()
                intent3.setClass(this@MainActivity, SubActivity::class.java)
                intent3.putExtras(bundle1)


                //test
                var bundle2 = bundleOf(
                        Pair("", ""),
                        Pair("232", 2))
                var intentTest = Intent()
                intentTest.setClass(this@MainActivity, SubActivity::class.java)
                intentTest.putExtras(bundle2)


                //way4
                startActivity<SubActivity>()
            }
            else -> {
                toast(getString(R.string.app_name))
            }

        }
    }


    companion object {
        private const val TAG: String = "MainActivity"
    }
}

