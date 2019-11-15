package com.smasher.rejuvenation.helper

import android.content.Context
import android.os.Build

import com.smasher.core.utils.NotificationUtil

/**
 * @author matao
 * @date 2019/6/14
 */
class NotificationChannelHelper(private var mContext: Context) {


    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createAlarmChannel(mContext)
            NotificationUtil.createMediaChannel(mContext)
        }
    }
}
