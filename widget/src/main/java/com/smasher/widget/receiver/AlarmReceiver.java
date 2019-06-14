package com.smasher.widget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.smasher.widget.nitifications.AlarmNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author matao
 * @date 2019/5/15
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String timeFormat = format.format(new Date(time));

        Log.d(TAG, "onReceive: " + timeFormat);
        Toast.makeText(context, "arrive", Toast.LENGTH_SHORT).show();
        AlarmNotification alarmNotification = new AlarmNotification(context);
        alarmNotification.notify("闹钟来了:" + timeFormat, 20);
    }
}
