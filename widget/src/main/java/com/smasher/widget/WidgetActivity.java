package com.smasher.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.smasher.widget.behavior.BehaviorActivity;



public class WidgetActivity extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widge);
        initView();
        initListener();
    }

    private void initListener() {
        start.setOnClickListener(mOnClickListener);
    }

    private void initView() {
        start = findViewById(R.id.start);
    }


    View.OnClickListener mOnClickListener = v -> {
        Intent intent = new Intent();
//        intent.setClass(this, BasicActivity.class);
        intent.setClass(this, BehaviorActivity.class);
        startActivity(intent);
    };
}
