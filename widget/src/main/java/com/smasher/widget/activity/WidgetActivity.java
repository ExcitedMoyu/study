package com.smasher.widget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.R;
import com.smasher.widget.behavior.BehaviorActivity;
import com.smasher.widget.helper.AnimationExDrawable;
import com.smasher.widget.helper.ThemeTransformHelper;


public class WidgetActivity extends AppCompatActivity {

    private static final String TAG = "WidgetActivity";
    Button start;
    Button behavior;
    ImageView mImageView;

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
        mImageView = findViewById(R.id.day_and_night);
    }

    int count = 0;

    View.OnClickListener mOnClickListener = v -> {

        int id = v.getId();
        if (id == R.id.start) {
            try {
                boolean isDay = count % 2 > 0;
                int type = isDay ? ThemeTransformHelper.TYPE_DAY_NIGHT : ThemeTransformHelper.TYPE_NIGHT_DAY;
                AnimationExDrawable drawable2 = ThemeTransformHelper.loadDayNightAnimationDrawable(this, type, 2000);
                if (drawable2 != null) {
                    drawable2.setOneShot(true);
                    drawable2.setAnimationFinishListener(() ->
                            Log.d(TAG, "drawable2 animation finished: ")
                    );
                    drawable2.start();
                }
                mImageView.setImageDrawable(drawable2);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.behavior) {
            Intent intent = new Intent();
            //intent.setClass(this, BasicActivity.class);
            intent.setClass(this, BehaviorActivity.class);
            startActivity(intent);
        }

    };
}
