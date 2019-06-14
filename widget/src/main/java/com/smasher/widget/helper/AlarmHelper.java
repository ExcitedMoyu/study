package com.smasher.widget.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * @author matao
 * @date 2019/6/14
 */
public class AlarmHelper {

    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";

    Context mContext;

    public AlarmHelper(Context context) {
        mContext = context;
    }


    public void setAlarm() {
        Intent intent = new Intent(ALARM_EVENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long INTERVAL = 1000 * 60 * 2L;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
