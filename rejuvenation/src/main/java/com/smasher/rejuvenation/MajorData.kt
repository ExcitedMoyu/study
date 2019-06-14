package com.smasher.rejuvenation

import android.util.Log

/**
 * @author matao
 * @date 2019/6/14
 */
data class MajorData(var position: Int) {
    var image: String? = null

    init {
        image = "http://bookcover.yuewen.com/qdbimg/349573/$position/150"
        Log.d("MajorData TAG", position.toString())
    }
}