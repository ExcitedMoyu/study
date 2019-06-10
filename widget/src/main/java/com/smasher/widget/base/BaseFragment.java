package com.smasher.widget.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smasher.oa.core.other.BusProvider;
import com.smasher.widget.struct.FunctionManager;


/**
 * @author matao
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected FunctionManager mFunctionManager;
    protected Context mContext;
    protected int mPageIndex = 1;


    protected boolean mIsVisible;
    protected boolean mHasLoaded;
    protected boolean mHasPrepare;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onViewCreated: loading" + this.toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BusProvider.getInstance().register(this);
        Log.d(TAG, "onViewCreated: loading" + this.toString());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: loading" + this.toString());
        if (mIsVisible) {
            initData();
            Log.e(TAG, "onViewCreated: loading" + this.toString());
            mHasLoaded = true;
        }
        mHasPrepare = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.d(TAG, "onViewCreated: loading" + this.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
        Log.d(TAG, "onViewCreated: loading" + this.toString());
    }


    public FunctionManager getFunctionManager() {
        return mFunctionManager;
    }

    public void setFunctionManager(FunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }


    //懒加载

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = getUserVisibleHint();
        if (!mIsVisible || mHasLoaded || !mHasPrepare) {
            //不可见||加载过||View未创建
            StringBuilder tag = new StringBuilder();

            if (!mIsVisible) {
                tag.append("!mIsVisible").append(" ");
            }
            if (mHasLoaded) {
                tag.append("mHasLoaded").append(" ");
            }
            if (!mHasPrepare) {
                tag.append("!mHasPrepare").append(" ");
            }

            Log.d(TAG, "setUserVisibleHint: " + this.toString() + tag.toString());
            return;
        }
        lazyLoad();
    }

    private void lazyLoad() {
        Log.d(TAG, "onViewCreated: loading" + this.toString());
        initData();
    }

    /**
     * 加载数据
     */
    protected abstract void initData();


}
