package com.smasher.widget.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.smasher.widget.R;
import com.smasher.widget.receiver.AlarmReceiver;

import java.util.Calendar;


/**
 * AlarmManager的使用 以及Receiver
 *
 * @author matao
 */
public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "AlarmActivity";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";

    Button alarmTest;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        initView();
        initListener();
    }

    private void initListener() {
        alarmTest.setOnClickListener(mOnClickListener);
        textView.setOnClickListener(mOnClickListener);
    }

    private void initView() {
        alarmTest = findViewById(R.id.alarm_test);
        textView = findViewById(R.id.textView);
    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_EVENT);

        registerReceiver(mAlarmReceiver, intentFilter);
        registerReceiver(receiver, intentFilter);


    }


    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(AlarmActivity.this, "arrive", Toast.LENGTH_SHORT).show();
            if (alarmTest != null) {
                String test = String.valueOf(System.currentTimeMillis());
                textView.setText(test);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }


    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        unregisterReceiver(receiver);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAlarmReceiver);
    }


    String mDesc = "";
    int mDelay = 20;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.alarm_test) {
                Intent intent12 = new Intent();
                intent12.setAction(ALARM_EVENT);
                sendBroadcast(intent12);

                Intent intent = new Intent(ALARM_EVENT);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.add(Calendar.SECOND, mDelay);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                mDesc = System.currentTimeMillis() + " 设置闹钟";
                alarmTest.setText(mDesc);
            } else if (i == R.id.textView) {
                Log.d(TAG, "textView: ");
            }
        }
    };

}
