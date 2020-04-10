package com.smasher.widget.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smasher.core.other.BusProvider;
import com.smasher.widget.struct.FunctionManager;


/**
 * @author matao
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected FunctionManager mFunctionManager;
    protected Context mContext;
    protected boolean mHasLoaded;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: loading" + this.toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BusProvider.getInstance().register(this);
        Log.d(TAG, "onActivityCreated: " + this.toString());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutRes(), container, false);
        initButterKnife(view);
        initView();
        return view;
    }

    protected void initButterKnife(View view) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + this.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.d(TAG, "onAttach: " + this.toString());
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mHasLoaded) {
            lazyLoad();
            mHasLoaded = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
        Log.d(TAG, "onDestroyView: " + this.toString());
    }


    public FunctionManager getFunctionManager() {
        return mFunctionManager;
    }

    public void setFunctionManager(FunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }


    //懒加载
    private void lazyLoad() {
        Log.d(TAG, "lazyLoad: loading" + this.toString());
        initData();
    }

    /**
     * 加载数据
     */
    protected abstract void initData();


    /**
     * 初始化页面
     */
    protected abstract void initView();


    /**
     * 构建页面
     *
     * @return LayoutRes
     */
    protected abstract int getLayoutRes();


}
