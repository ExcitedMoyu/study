package com.smasher.rejuvenation.activity.activity.basic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.core.utils.DensityUtil;
import com.smasher.rejuvenation.R;
import com.smasher.rejuvenation.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * @author matao
 * @date 2019/5/29
 */
public class MFragment extends BaseFragment {

    private static final String TAG = "MFragment";
    private String mPosition;
    private RecyclerView recyclerView;
    private MoveAdapter mAdapter;
    private List<Integer> mList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    protected void initData() {
        Log.d(TAG, "child initData: ");
        if (mList == null) {
            mList = new ArrayList<>();
        }

        for (int i = 0; i < 100; i++) {
            mList.add(i);
        }
        if (mAdapter != null) {
            mAdapter.setData(mList);
            mAdapter.notifyDataSetChanged();
        }
        mHasLoaded = true;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_basic;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        mPosition = "-1";

        if (bundle == null) {
            return;
        }

        if (bundle.containsKey("index")) {
            mPosition = bundle.getString("index", "-1");
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        if (context != null) {
            mAdapter = new MoveAdapter(context);
            mAdapter.setData(mList);
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
            ItemTouchHelper.Callback callback = new IHCallback(mAdapter, DensityUtil.dip2px(context, 80));
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
