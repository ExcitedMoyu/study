package com.smasher.media.helper;


import android.app.Notification;
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
import android.util.Log;

import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.Builder;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.smasher.media.R;
import com.smasher.media.constant.Constant;
import com.smasher.media.service.MediaService;
import com.smasher.oa.core.utils.NotificationUtil;

import java.util.Random;


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
            NotificationUtil.createMediaChannel(mContext);
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
        Action list = createAction(R.drawable.music_list, "like", Constant.ACTION_LIST);
        Action like = createAction(R.drawable.music_like_black, "like", Constant.ACTION_LIKE);
        Action next = createAction(R.drawable.music_next, "next", Constant.ACTION_NEXT);
        Action previous = createAction(R.drawable.music_previous, "previous", Constant.ACTION_PREVIOUS);
        MediaMetadataCompat data = mSession.getController().getMetadata();
        MediaDescriptionCompat description = null;
        if (data != null) {
            description = data.getDescription();
        }


        Bitmap bitmap = getLargeIcon();
        mBuilder = new Builder(mContext, Constant.CHANNEL_ID);
        mBuilder.setTicker("music")

                .setLargeIcon(bitmap)
                .addAction(list)
                .addAction(previous)
                .addAction(playPauseAction)
                .addAction(next)
                .addAction(like)
                .setShowWhen(false)
                .setColor(Color.parseColor("#ed424b"))
                .setColorized(true)
                .setStyle(mMediaStyle)
                .setContentTitle(description != null ? description.getTitle() : "播放器前台服务")
                .setContentText(description != null ? description.getSubtitle() : "服务运行中...")
                .setSmallIcon(R.drawable.ic_stat_name);


        mMediaStyle.setShowActionsInCompactView(1, 2, 3);
        mMediaStyle.setShowCancelButton(true);
        mMediaStyle.setBuilder(mBuilder);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }
        return mBuilder.build();
    }

    private Bitmap getLargeIcon() {
        Bitmap bitmap;
        Random random = new Random();
        int position = random.nextInt(4);
        switch (position) {
            case 0:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu4);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu1);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu2);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu3);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu4);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mumu4);
                break;
        }

        return bitmap;
    }


}
