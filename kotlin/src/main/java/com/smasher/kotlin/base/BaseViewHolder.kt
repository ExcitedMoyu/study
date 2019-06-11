package com.smasher.kotlin.base

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author matao
 * @date 2019/4/4
 */
open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {


    protected var mItem: T? = null
    protected var mIndex: Int = 0
    protected var mContext: Context


    init {

    }


    private val itemListener = View.OnClickListener { v ->
        Log.d(TAG, v.id.toString())
    }

    init {
        mContext = itemView.context
    }


    fun setIndex(mIndex: Int) {
        this.mIndex = mIndex
    }


    fun setItem(mItem: T) {
        this.mItem = mItem
    }


    open fun bindView() {
        if (mItem != null) {
            itemView.setOnClickListener(itemListener)
        }
    }


    fun getString(res: Int): String {
        return mContext.getString(res)
    }

    companion object {
        private const val TAG: String = "IntegerViewHolder"
    }

}
