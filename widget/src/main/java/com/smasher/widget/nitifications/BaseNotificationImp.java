package com.smasher.widget.nitifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * @author matao
 * @date 2019/6/14
 */
public abstract class BaseNotificationImp {

    public NotificationManager mManager;
    public Context mContext;

    public BaseNotificationImp(Context context) {
        mContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public abstract void notify(int id, Notification notification);

    public abstract void cancel(int id, Notification notification);

}
