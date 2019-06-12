package com.smasher.media.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smasher.media.R;
import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.base.BaseActivity;

/**
 * @author moyu
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
        }
    }

    @Override
    public void setFunctionsForFragment(String tag) {

    }

    @Override
    public View getRootView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_medias, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

}
