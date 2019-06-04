package com.smasher.widget.behavior;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smasher.widget.R;

/**
 * @author matao
 * @date 2019/6/4
 */
public class StringAdapter extends BaseRecyclerViewAdapter<String, StringViewHolder> {

    public StringAdapter(Context context) {
        super(context);
    }

    @Override
    public StringViewHolder onCreateDefineViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_content, viewGroup, false);
        return new StringViewHolder(view);
    }
}
