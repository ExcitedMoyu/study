package com.smasher.rejuvenation.entity

import android.util.Log

/**
 * @author matao
 * @date 2019/6/14
 */
data class MajorData(var mPosition: Int) {
    companion object {
        private const val TAG: String = "MajorData"
    }

    init {
        Log.d(TAG, "init")
    }

    /**
     * 次构造函数
     */
    constructor(position: Int, index: Int) : this(position) {
        mPosition = position
    }


    var image: String? = null

    init {
        image = "http://bookcover.yuewen.com/qdbimg/349573/$mPosition/150"
        Log.d("MajorData TAG", mPosition.toString())
    }
}