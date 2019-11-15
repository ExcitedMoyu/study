package com.smasher.widget.activity;

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

import com.smasher.core.utils.StatusBarUtil;
import com.smasher.widget.R;
import com.smasher.widget.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * AlarmManager的使用 以及Receiver
 *
 * @author matao
 */
public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "AlarmActivity";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";
    Button alarmIntent;
    Button alarmTest;
    TextView textView;
    String mDesc = "设置闹钟";
    String mTestButton = "发送意图";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        StatusBarUtil.setTranslucent(this);

        initView();
        initListener();
    }

    private void initListener() {
        alarmIntent.setOnClickListener(mOnClickListener);
        alarmTest.setOnClickListener(mOnClickListener);
        textView.setOnClickListener(mOnClickListener);
    }

    private void initView() {
        alarmTest = findViewById(R.id.alarm_test);
        alarmIntent = findViewById(R.id.alarm_intent);
        textView = findViewById(R.id.textView);

        alarmTest.setText(mDesc);
        alarmIntent.setText(mTestButton);
    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_EVENT);
        registerReceiver(mAlarmReceiver, intentFilter);
//        registerReceiver(receiver, intentFilter);


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
//        unregisterReceiver(receiver);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAlarmReceiver);
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = view.getId();
            if (i == R.id.alarm_test) {

                Intent intent = new Intent(ALARM_EVENT);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 16);
                calendar.set(Calendar.MINUTE, 30);
                calendar.set(Calendar.SECOND, 0);
                long INTERVAL = 1000 * 60 * 2L;
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, pendingIntent);

                StringBuilder desc = new StringBuilder("设置闹钟");
                Date date = new Date(System.currentTimeMillis());
                String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
                String dataString = simpleDateFormat.format(date);
                desc.append("时间:  ").append(dataString).append("\n");

                textView.setText(desc.toString());
            } else if (i == R.id.alarm_intent) {
                Intent intent = new Intent(ALARM_EVENT);
                sendBroadcast(intent);
                Log.d(TAG, "send intent: ");
            } else if (i == R.id.textView) {
                Log.d(TAG, "textView: ");
            }
        }
    };

}
