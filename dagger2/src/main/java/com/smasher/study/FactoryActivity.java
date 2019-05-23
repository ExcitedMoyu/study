package com.smasher.study;

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.smasher.study.entity.Product;
import com.smasher.study.interfaces.DaggerFactoryActivityComponent;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author matao
 */
public class FactoryActivity extends AppCompatActivity {

    private static final String TAG = "FactoryActivity";
    private static final String ALARM_EVENT = "com.smasher.study.AlarmEvent";
    @Inject
    Product mProduct;
    @BindView(R.id.alarm_test)
    Button alarmTest;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);
        ButterKnife.bind(this);

        DaggerFactoryActivityComponent.builder().build().inject(this);

        //mProduct = new Product();
        mProduct.setName("測試");

        Toast.makeText(this, mProduct.getName(), Toast.LENGTH_SHORT).show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_EVENT);
        registerReceiver(mAlarmReceiver, intentFilter);
    }


    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ALARM_EVENT);
        registerReceiver(receiver, intentFilter);


    }


    private AlarmReceiver mAlarmReceiver = new AlarmReceiver();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(FactoryActivity.this, "arrive", Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.alarm_test, R.id.textView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alarm_test:


                Intent intent12 = new Intent();
                intent12.setAction(ALARM_EVENT);
                sendBroadcast(intent12);


                Intent intent = new Intent(ALARM_EVENT);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.add(Calendar.SECOND, mDelay);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                mDesc = System.currentTimeMillis() + " 设置闹钟";
                alarmTest.setText(mDesc);
                break;
            case R.id.textView:
                break;
            default:
                break;
        }
    }

}
