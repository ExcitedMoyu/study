package com.smasher.widget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.smasher.core.utils.StatusBarUtil;
import com.smasher.widget.R;
import com.smasher.widget.behavior.BehaviorActivity;


/**
 * @author matao
 */
public class WidgetActivity extends AppCompatActivity {

    private static final String TAG = "WidgetActivity";
    private Button start;
    private Button behavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        StatusBarUtil.setTranslucent(this);
        initView();
        initListener();
    }

    private void initListener() {
        start.setOnClickListener(mOnClickListener);
        behavior.setOnClickListener(mOnClickListener);
    }

    private void initView() {
        behavior = findViewById(R.id.behavior);
        start = findViewById(R.id.start);

    }

    View.OnClickListener mOnClickListener = v -> {

        int id = v.getId();
        if (id == R.id.behavior) {
            Intent intent = new Intent();
            //intent.setClass(this, IndicatorActivity.class);
            intent.setClass(this, BehaviorActivity.class);
            startActivity(intent);
        }
    };
}
