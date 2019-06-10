package com.smasher.oa.core.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

/**
 * Created on 2019/6/9.
 *
 * @author moyu
 */
public class NotificationUtil {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "CHANNEL_Default";


    /**
     * 创建通知渠道。Android 8.0开始必须给每个通知分配对应的渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public static void createNotifyChannel(Context ctx, String channelId, String channelName) {

        if (TextUtils.isEmpty(channelId)) {
            channelId = CHANNEL_ID;
        }

        if (TextUtils.isEmpty(channelName)) {
            channelName = CHANNEL_NAME;
        }


        // 创建一个默认重要性的通知渠道
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_DEFAULT);
        // 设置推送通知之时的铃声。null表示静音推送
        channel.setSound(null, null);
        // 震动
        channel.enableVibration(false);
        // 设置在桌面图标右上角展示小红点
        channel.enableLights(true);
        // 设置小红点的颜色
        channel.setLightColor(Color.RED);
        // 在长按桌面图标时显示该渠道的通知
        channel.setShowBadge(true);
        // 从系统服务中获取通知管理器
        NotificationManager mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建指定的通知渠道
        mNotificationManager.createNotificationChannel(channel);

    }
}
