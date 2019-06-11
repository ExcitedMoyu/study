package com.smasher.kotlin.base

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @author moyu
 */
abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(context: Context) : RecyclerView.Adapter<VH>() {
    private var mList: MutableList<T>? = null

    protected var mContext: Context

    init {
        Log.d(TAG, "init")
        mContext = context
    }


    open fun setData(list: MutableList<T>?) {
        if (list != null) {
            mList = list
        } else {
            mList = ArrayList()
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): VH {
        return createDefineViewHolder(viewGroup, type)
    }


    abstract fun createDefineViewHolder(viewGroup: ViewGroup, type: Int): VH


    open override fun onBindViewHolder(viewHolder: VH, position: Int) {
        if (mList!!.isEmpty() || position < 0 || position >= mList!!.size) {
            return
        }
        val item = mList!![position] ?: return
        viewHolder.setIndex(position)
        viewHolder.setItem(item)
        viewHolder.bindView()
    }

    open override fun getItemCount(): Int {
        return if (mList == null) {
            0
        } else mList!!.size
    }

    companion object {
        const val TAG = "Adapter"
    }


}
