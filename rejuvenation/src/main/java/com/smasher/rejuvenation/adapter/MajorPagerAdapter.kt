package com.smasher.rejuvenation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.smasher.rejuvenation.R

class MajorPagerAdapter(private val mContext: Context) : PagerAdapter() {

    private val mList: MutableList<View> = mutableListOf()
    private var mData: MutableList<Int> = mutableListOf()

    public fun setData(data: MutableList<Int>) {
        mData = data
    }

    override fun getCount(): Int {
        return mData.size
    }


    override fun getPageWidth(position: Int): Float {
        return 1.0f
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position)
        val view: View = initView(position)
        container.addView(view)
        return view
    }

    private fun initView(position: Int): View {
        if (position >= 0 && position < mList.size) {
            return mList[position]
        }
        val view = createView(position)
        mList.add(position, view)
        return view
    }

    private fun createView(position: Int): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_banner_image, null)
        val image: ImageView = view.findViewById(R.id.pager_image)
        image.setImageResource(mData[position])
        //Glide.with(mContext).load(mData[position]).into(image)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        Log.d(TAG, `object`.toString())
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` is View && view == `object`
    }

    override fun startUpdate(container: ViewGroup) {
        super.startUpdate(container)
    }

    override fun finishUpdate(container: ViewGroup) {
        super.finishUpdate(container)
    }

    companion object {
        private const val TAG: String = "MajorPagerAdapter"
    }
}