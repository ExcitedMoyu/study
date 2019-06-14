package com.smasher.rejuvenation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.smasher.rejuvenation.MajorData
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.holder.MajorViewHolder
import com.smasher.widget.base.BaseRecyclerViewAdapter

/**
 * @author matao
 * @date 2019/6/14
 */
class MajorAdapter(context: Context) : BaseRecyclerViewAdapter<MajorData, MajorViewHolder>(context) {


    override fun onCreateDefineViewHolder(viewGroup: ViewGroup, type: Int): MajorViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_major, viewGroup, false)
        return MajorViewHolder(view)
    }
}