package com.smasher.widget.slideswaphelper.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.oa.core.utils.StatusBarUtil;
import com.smasher.widget.R;
import com.smasher.widget.base.BaseActivity;
import com.smasher.widget.slideswaphelper.PlusItemSlideCallback;
import com.smasher.widget.slideswaphelper.WItemTouchHelperPlus;

import java.util.ArrayList;
import java.util.List;


public class SlideMenuActivity extends BaseActivity {


    RecyclerView mRecyclerView;
    SlideAdapter mAdapter;
    List<Object> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public View getRootView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_slide_menu, null);
    }

    @Override
    public void initView() {

        mAdapter = new SlideAdapter(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        PlusItemSlideCallback callback = new PlusItemSlideCallback();
        callback.setType(WItemTouchHelperPlus.SLIDE_ITEM_TYPE_ITEMVIEW);
        WItemTouchHelperPlus extension = new WItemTouchHelperPlus(callback);
        extension.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void setFunctionsForFragment(String tag) {

    }

    @Override
    public void initData() {
        mList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            mList.add(i);
        }
        mAdapter.setData(mList);
    }

}
