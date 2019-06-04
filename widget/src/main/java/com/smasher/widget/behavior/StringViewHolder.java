package com.smasher.widget.behavior;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.smasher.widget.R;

/**
 * @author matao
 * @date 2019/6/4
 */
public class StringViewHolder extends BaseRecyclerViewHolder<String> {
    private TextView content;

    public StringViewHolder(@NonNull View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.content);
    }

    @Override
    public void bindView() {
        super.bindView();
        content.setText(mItem);
    }
}
