package com.smasher.widget.activity.basic;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.smasher.widget.R;
import com.smasher.widget.base.BaseRecyclerViewHolder;

/**
 * @author matao
 * @date 2019/6/10
 */
public class MoveViewHolder extends BaseRecyclerViewHolder<Integer> {
    private TextView mTextView;

    public MoveViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
    }


    @Override
    public void bindView() {
        super.bindView();
        mTextView.setText(mIndex + "、content in item");
    }
}
