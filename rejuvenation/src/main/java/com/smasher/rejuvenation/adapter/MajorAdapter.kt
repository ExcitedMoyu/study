package com.smasher.rejuvenation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smasher.rejuvenation.entity.MajorData
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.holder.BaseMajorViewHolder
import com.smasher.rejuvenation.holder.MajorCommonViewHolder
import com.smasher.rejuvenation.holder.MajorPagerExViewHolder
import com.smasher.rejuvenation.holder.MajorPagerViewHolder
import com.smasher.rejuvenation.base.BaseRecyclerViewAdapter

/**
 * @author matao
 * @date 2019/6/14
 */
class MajorAdapter(context: Context) : BaseRecyclerViewAdapter<MajorData, BaseMajorViewHolder>(context) {


    override fun onCreateDefineViewHolder(viewGroup: ViewGroup, type: Int): BaseMajorViewHolder {
        var viewHolder = BaseMajorViewHolder(View(mContext))
        when (type) {
            0 -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_major, viewGroup, false)
                viewHolder = MajorCommonViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_major_pager, viewGroup, false)
                viewHolder = MajorPagerViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_major_pager_ex, viewGroup, false)
                viewHolder = MajorPagerExViewHolder(view)
            }
        }
        return viewHolder
    }


    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        return when (position) {
            0 -> 1
            6 -> 2
            else -> 0
        }
    }
}