package com.smasher.widget.activity.behavior;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.smasher.widget.R;

/**
 * @author matao
 */
@Deprecated
public class BehaviorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
    }
}