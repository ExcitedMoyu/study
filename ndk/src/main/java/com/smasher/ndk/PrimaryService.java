package com.smasher.ndk;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.smasher.widget.receiver.AlarmReceiver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2019/6/5.
 *
 * @author moyu
 */
public class PrimaryService extends Service {

    public int i = 0;
    private static final String TAG = "PrimaryService";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";


    AlarmReceiver mAlarmReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        Watcher watcher = new Watcher();
        watcher.createWatcher(String.valueOf(Process.myUid()));
        watcher.connectMonitor();

        ScheduledExecutorService sss = Executors.newScheduledThreadPool(3);
        sss.scheduleAtFixedRate(() -> {
            Log.d(TAG, "服务开启中" + i);
            i++;
        }, 0, 3, TimeUnit.SECONDS);

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ALARM_EVENT);
        mAlarmReceiver = new AlarmReceiver();
        registerReceiver(mAlarmReceiver, mFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
