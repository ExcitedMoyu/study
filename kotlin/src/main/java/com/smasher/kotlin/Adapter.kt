package com.smasher.kotlin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author matao
 * @date 2019/5/23
 */
class Adapter(context: Context) : BaseAdapter<Int, IntegerViewHolder<Int>>(context) {

    init {
        Log.d(TAG, "init")
    }

    override fun createDefineViewHolder(viewGroup: ViewGroup, type: Int): IntegerViewHolder<Int> {
        val viewTemp: View = LayoutInflater.from(mContext).inflate(R.layout.layout_item, viewGroup, false)
        return IntegerViewHolder(viewTemp)
    }


}
