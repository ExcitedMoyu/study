package com.smasher.kotlin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smasher.kotlin.R
import com.smasher.kotlin.base.BaseAdapter
import com.smasher.kotlin.entity.DDD
import com.smasher.kotlin.holder.IntegerViewHolder

/**
 * @author matao
 * @date 2019/5/23
 */
class Adapter(context: Context) : BaseAdapter<DDD, IntegerViewHolder>(context) {

    init {
        Log.d(TAG, "init")
    }

    override fun createDefineViewHolder(viewGroup: ViewGroup, type: Int): IntegerViewHolder {
        val viewTemp: View = LayoutInflater.from(mContext).inflate(R.layout.layout_item, viewGroup, false)
        return IntegerViewHolder(viewTemp)
    }


}
