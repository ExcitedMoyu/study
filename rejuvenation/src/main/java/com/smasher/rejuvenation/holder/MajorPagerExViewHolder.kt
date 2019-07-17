package com.smasher.rejuvenation.holder

import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.smasher.draw.view.IndicatorView
import com.smasher.oa.core.utils.DensityUtil
import com.smasher.rejuvenation.R
import com.smasher.rejuvenation.adapter.MajorPagerAdapter


class MajorPagerExViewHolder(itemView: View) : BaseMajorViewHolder(itemView), Handler.Callback {

    private val mViewPager: ViewPager = itemView.findViewById(R.id.viewPager)
    private val mIndicator: IndicatorView = itemView.findViewById(R.id.indicator)
    private var mAdapter: MajorPagerAdapter? = null
    private var currentIndex = 1 //当前位置
    private val mHandler: Handler = Handler(this)
    private val autoPlay = true //是否自动播放

    init {
        Log.d(TAG, "")
        val address: MutableList<Int> = mutableListOf(
                R.drawable.image_sss1, R.drawable.image_sss2, R.drawable.image_sss3,
                R.drawable.image_sss4, R.drawable.image_sss5, R.drawable.image_sss6
        )
        mAdapter = MajorPagerAdapter(itemView.context)
        mAdapter!!.setData(address)

        val currentParam: ConstraintLayout.LayoutParams =
                mViewPager.layoutParams as ConstraintLayout.LayoutParams
        val margin = DensityUtil.dip2px(mContext, 24f)
        currentParam.setMargins(margin, 0, margin, margin)
        mIndicator.init(0, 6)
        mIndicator.setColorResourceId(R.color.color_838A96, R.color.color_ED424B)
    }

    override fun bindView() {
        super.bindView()
        mViewPager.pageMargin = mContext.resources.getDimensionPixelSize(R.dimen.length_12)
        mViewPager.offscreenPageLimit = 3
        mViewPager.adapter = mAdapter
        mViewPager.addOnPageChangeListener(onPageChangeListener)
        play()
    }

    private val runnable = Runnable {
        currentIndex++
        if (currentIndex >= mAdapter!!.count) {
            currentIndex = 0
        }
        mViewPager.currentItem = currentIndex
    }

    override fun handleMessage(msg: Message?): Boolean {
        play()
        return false
    }

    private val onPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        mViewPager.setCurrentItem(currentIndex, false)
                        play()
                    } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        //cancel()
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    mIndicator.setPosition(position)
                }

            }

    /**
     * 播放，根据autoplay
     */
    private fun play() {
        if (autoPlay) {
            mHandler.postDelayed(runnable, time)
        } else {
            mHandler.removeCallbacks(runnable)
        }
    }

    companion object {
        private const val TAG: String = "MajorPagerViewHolder"
        private const val time: Long = 3000 //自动播放时间
    }
}