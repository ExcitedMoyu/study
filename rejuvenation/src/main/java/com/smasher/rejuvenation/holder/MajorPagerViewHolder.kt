package com.smasher.rejuvenation.holder

import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.adapter.MajorAdapter
import com.smasher.rejuvenation.adapter.MajorPagerAdapter
import kotlinx.android.synthetic.main.item_major_pager.view.*

class MajorPagerViewHolder(itemView: View) : BaseMajorViewHolder(itemView) {
    private val mViewPager: ViewPager = itemView.findViewById(R.id.viewPager)
    private var mAdapter: MajorPagerAdapter? = null

    init {
        Log.d(TAG, "")
        val address: MutableList<Int> = mutableListOf(
                R.drawable.image_sss1, R.drawable.image_sss2, R.drawable.image_sss3,
                R.drawable.image_sss4, R.drawable.image_sss5, R.drawable.image_sss6
        )
        mAdapter = MajorPagerAdapter(itemView.context)
        mAdapter!!.setData(address)
    }

    override fun bindView() {
        super.bindView()
        mViewPager.pageMargin = mContext.resources.getDimensionPixelSize(R.dimen.length_12)
        mViewPager.offscreenPageLimit = 3
        mViewPager.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }

    companion object {
        private const val TAG: String = "MajorPagerViewHolder"
    }
}