package com.smasher.kotlin.holder

import android.util.Log
import android.view.View
import android.widget.TextView
import com.smasher.kotlin.R
import com.smasher.kotlin.base.BaseViewHolder
import com.smasher.kotlin.entity.DDD

/**
 * @author matao
 * @date 2019/5/23
 */
class IntegerViewHolder(itemView: View) : BaseViewHolder<DDD>(itemView) {


    private var mTextView: TextView

    init {
        Log.d(TAG, "IntegerViewHolder init")
        mTextView = itemView.findViewById(R.id.item_count) as TextView
    }

    override fun bindView() {
        if (mItem != null) {
            mTextView.text = mItem!!.name
        }
    }

    companion object {
        private const val TAG: String = "IntegerViewHolder"
    }
}