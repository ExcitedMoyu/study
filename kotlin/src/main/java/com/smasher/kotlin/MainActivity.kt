package com.smasher.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

import org.jetbrains.anko.toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var editWatcher: EditWatcher = EditWatcher()
    private var mAdapter: Adapter = Adapter(this)
    private var mList: MutableList<Int> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        study.text = getString(R.string.app_name)
        study.setOnClickListener { toast(getString(R.string.app_name)) }

        editText.addTextChangedListener(editWatcher)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        for (i in 0 until 100) {
            mList.add(i)
        }

        mAdapter = Adapter(this)
        mAdapter.setData(mList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }


    private inner class EditWatcher : TextWatcher {

        init {
            Log.d(TAG, "EditWatcher init")
        }

        override fun afterTextChanged(s: Editable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    companion object {
        private val TAG: String = "MainActivity"
    }
}
