package com.smasher.core.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created on 2019/6/9.
 *
 * @author moyu
 */
public class NotificationUtil {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "CHANNEL_Default";


    public static final String CHANNEL_ID_MEDIA = "ForegroundServiceChannelId";
    public static final String CHANNEL_NAME_MEDIA = "ForegroundService";

    public static final String CHANNEL_ID_ALARM = "AlarmChannelId";
    public static final String CHANNEL_NAME_ALARM = "AlarmMessage";

    /**
     * 创建通知渠道。Android 8.0开始必须给每个通知分配对应的渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public static void createMediaChannel(Context ctx) {

        // 创建一个默认重要性的通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_MEDIA,
                CHANNEL_NAME_MEDIA, NotificationManager.IMPORTANCE_DEFAULT);
        // 设置推送通知之时的铃声。null表示静音推送
        channel.setSound(null, null);
        // 震动
        channel.enableVibration(false);
        // 设置在桌面图标右上角展示小红点
        channel.enableLights(false);
//        // 设置小红点的颜色
//        channel.setLightColor(Color.RED);

//        // 在长按桌面图标时显示该渠道的通知
//        channel.setShowBadge(true);
        // 从系统服务中获取通知管理器
        NotificationManager mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建指定的通知渠道
        mNotificationManager.createNotificationChannel(channel);
    }


    /**
     * 创建通知渠道。Android 8.0开始必须给每个通知分配对应的渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public static void createAlarmChannel(Context ctx) {

        // 创建一个默认重要性的通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_ALARM,
                CHANNEL_NAME_ALARM, NotificationManager.IMPORTANCE_DEFAULT);
//        Uri uri = RingtoneManager
//                .getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_ALARM);

        Uri uri = Uri.parse("/storage/emulated/0/qqmusic/song/Alan Walker - Fade [mqms2].flac");
        //Uri uri = Uri.parse("/storage/emulated/0/Music/曲婉婷 - 我的歌声里.mp3");
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setUsage(AudioAttributes.USAGE_ALARM);
        builder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        AudioAttributes audioAttributes = builder.build();
        // 设置推送通知之时的铃声。null表示静音推送
        channel.setSound(uri, audioAttributes);
        // 震动
        channel.enableVibration(true);
        // 设置在桌面图标右上角展示小红点
        channel.enableLights(true);
//        // 设置小红点的颜色
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
