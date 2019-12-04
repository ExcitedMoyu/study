package com.smasher.widget.activity.swap.test;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.widget.R;
import com.smasher.widget.activity.swap.PlusItemSlideCallback;
import com.smasher.widget.activity.swap.WItemTouchHelperPlus;
import com.smasher.widget.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author warrior
 */
public class SlideMenuActivity extends BaseActivity {


    RecyclerView mRecyclerView;
    SlideAdapter mAdapter;
    List<Object> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBarUtil.setTranslucent(this, 1);
    }

    @Override
    public int getRootViewRes() {
        return R.layout.activity_slide_menu;
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
    public void initData() {
        mList = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            mList.add(i);
        }
        mAdapter.setData(mList);
    }

}