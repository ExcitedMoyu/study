package com.smasher.media.helper;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.MediaSessionCompat.Token;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.smasher.media.R;
import com.smasher.media.constant.Constant;
import com.smasher.media.service.MediaService;


/**
 * @author matao
 * @date 2019/6/4
 */
public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    private Context mContext;
    private Builder mBuilder;
    private NotificationManager mNotificationManager;
    private Token mToken;
    private MediaSessionCompat mSession;
    private MediaStyle mMediaStyle;

    public NotificationHelper(Context context, MediaSessionCompat session) {
        mContext = context;
        mSession = session;
        mToken = mSession.getSessionToken();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mMediaStyle = new MediaStyle();
        mMediaStyle.setMediaSession(mToken);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotifyChannel(mContext, null, null);
        }
    }

    private Action createAction(int iconResId, String title, String action) {
        Intent intent = new Intent();
        intent.setClass(mContext, MediaService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 1, intent, 0);
        return new Action(iconResId, title, pendingIntent);
    }


    public void updateNotification() {
        Log.d(TAG, "updateNotification: ");
        Notification notification = createNotification();
        mNotificationManager.notify(Constant.NOTIFICATION_ID, notification);
    }


    public Notification createNotification() {
        boolean playing = mSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING;
        Log.d(TAG, "createNotification: isPlaying " + playing);
        Action playPauseAction = null;
        if (playing) {
            playPauseAction = createAction(R.drawable.music_pause, "Pause", Constant.ACTION_PAUSE);
        } else {
            playPauseAction = createAction(R.drawable.music_play_small, "play", Constant.ACTION_PLAY);
        }
        Action like_red = createAction(R.drawable.music_like_red, "like", Constant.ACTION_LIKE);
        Action like = createAction(R.drawable.music_like_black, "like", Constant.ACTION_LIKE);
        Action next = createAction(R.drawable.music_next, "next", Constant.ACTION_NEXT);
        Action previous = createAction(R.drawable.music_previous, "previous", Constant.ACTION_PREVIOUS);
        MediaMetadataCompat data = mSession.getController().getMetadata();
        MediaDescriptionCompat description = null;
        if (data != null) {
            description = data.getDescription();
        }
        mMediaStyle.setShowActionsInCompactView(1, 2, 3);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sea);
        mBuilder = new Builder(mContext, Constant.CHANNEL_ID);
        mBuilder.setTicker("music")
                .setLargeIcon(bitmap)
                .addAction(like_red)
                .addAction(previous)
                .addAction(playPauseAction)
                .addAction(next)
                .addAction(like)
                .setShowWhen(false)

                .setStyle(mMediaStyle)
                .setContentTitle(description != null ? description.getTitle() : "播放器前台服务")
                .setContentText(description != null ? description.getSubtitle() : "服务运行中...")
                .setSmallIcon(R.drawable.ic_stat_name);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }
        return mBuilder.build();
    }


    /**
     * 创建通知渠道。Android 8.0开始必须给每个通知分配对应的渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public static void createNotifyChannel(Context ctx, String channelId, String channelName) {

        if (TextUtils.isEmpty(channelId)) {
            channelId = Constant.CHANNEL_ID;
        }

        if (TextUtils.isEmpty(channelName)) {
            channelName = Constant.CHANNEL_NAME;
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
