package com.smasher.ndk;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.smasher.oa.core.other.WeakReferenceHandler;
import com.smasher.oa.core.thread.ThreadPool;
import com.smasher.widget.helper.AlarmHelper;
import com.smasher.widget.receiver.AlarmReceiver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2019/6/5.
 *
 * @author moyu
 */
public class PrimaryService extends Service implements Handler.Callback {

    public int i = 0;
    private static final String TAG = "PrimaryService";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";


    AlarmReceiver mAlarmReceiver;
    AlarmHelper mAlarmHelper;
    WeakReferenceHandler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        mHandler = new WeakReferenceHandler(this);
        ThreadPool.getInstance(ThreadPool.PRIORITY_HIGH).submit(new Runnable() {
            @Override
            public void run() {

                try {
                    Watcher watcher = new Watcher();
                    watcher.createWatcher(String.valueOf(Process.myUid()));
                    watcher.connectMonitor();
                    ScheduledExecutorService sss = Executors.newScheduledThreadPool(3);
                    sss.scheduleAtFixedRate(() -> {
                        Log.d(TAG, "服务开启中" + i);
                        i++;
                    }, 0, 3, TimeUnit.SECONDS);
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ALARM_EVENT);
        mAlarmReceiver = new AlarmReceiver();
        registerReceiver(mAlarmReceiver, mFilter);

        mAlarmHelper = new AlarmHelper(this);
        mAlarmHelper.setAlarm();

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
        unregisterReceiver(mAlarmReceiver);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
