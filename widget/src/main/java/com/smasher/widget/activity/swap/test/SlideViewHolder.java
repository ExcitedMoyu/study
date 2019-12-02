package com.smasher.widget.activity.swap.test;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.smasher.core.utils.DensityUtil;
import com.smasher.widget.R;
import com.smasher.widget.base.BaseRecyclerViewHolder;
import com.smasher.widget.activity.swap.SlideSwapAction;

public class SlideViewHolder extends BaseRecyclerViewHolder<Object> implements SlideSwapAction {
    private TextView mItemText;
    private LinearLayout mSlide;
    private TextView mZhiding;
    private TextView mYidu;
    private TextView mShanchu;
    public RelativeLayout slideItem;

    public SlideViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemText = itemView.findViewById(R.id.item_text);
        mSlide = itemView.findViewById(R.id.slide);
        mZhiding = itemView.findViewById(R.id.zhiding);
        mYidu = itemView.findViewById(R.id.yidu);
        mShanchu = itemView.findViewById(R.id.shanchu);
        slideItem = itemView.findViewById(R.id.slide_itemView);
    }

    @Override
    public void bindView() {
        super.bindView();
        mItemText.setText(mItem.toString());
        mItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "s  " + mIndex, Toast.LENGTH_SHORT).show();
            }
        });

        mZhiding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "置顶" + mIndex, Toast.LENGTH_SHORT).show();
            }
        });
        mYidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "已读" + mIndex, Toast.LENGTH_SHORT).show();
            }
        });
        mShanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除" + mIndex, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public float getActionWidth() {
        return DensityUtil.dip2px(mContext, 240);
    }

    @Override
    public View ItemView() {
        return slideItem;
    }
}
