package com.smasher.widget.basic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.oa.core.utils.DensityUtil;
import com.smasher.widget.R;
import com.smasher.widget.base.BaseFragment;

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
    public void onResume() {
        super.onResume();
        if (!mHasLoaded) {
            initData();
        }

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_basic, container, false);
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
