package com.smasher.widget.slideswaphelper.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smasher.widget.R;
import com.smasher.widget.base.BaseRecyclerViewAdapter;

public class SlideAdapter extends BaseRecyclerViewAdapter<Object, SlideViewHolder> {

    public SlideAdapter(Context context) {
        super(context);
    }

    @Override
    public SlideViewHolder onCreateDefineViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_slide, viewGroup, false);
        return new SlideViewHolder(view);
    }
}
