package com.smasher.kotlin

import android.util.Log
import android.view.View
import android.widget.TextView

/**
 * @author matao
 * @date 2019/5/23
 */
class IntegerViewHolder<Int>(itemView: View) : BaseViewHolder<Int>(itemView) {


    private var mTextView: TextView

    init {
        Log.d(TAG, "IntegerViewHolder init")
        mTextView = itemView.findViewById(R.id.item_count) as TextView
    }

    override fun bindView() {
        if (mItem != null) {
            mTextView.text = mItem.toString()
        }
    }

    companion object {
        val TAG: String = "IntegerViewHolder"
    }
}