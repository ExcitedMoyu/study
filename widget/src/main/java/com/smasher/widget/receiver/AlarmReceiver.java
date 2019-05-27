package com.smasher.widget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author matao
 * @date 2019/5/15
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + System.currentTimeMillis());
        Toast.makeText(context, "arrive", Toast.LENGTH_SHORT).show();

    }
}
