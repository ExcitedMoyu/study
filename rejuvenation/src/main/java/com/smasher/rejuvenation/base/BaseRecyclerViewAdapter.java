package com.smasher.rejuvenation.base;

import android.content.Context;

/**
 * @author moyu
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewHolder<T>> extends com.smasher.widget.base.BaseRecyclerViewAdapter<T,VH> {


    public BaseRecyclerViewAdapter(Context context) {
        super(context);
    }

}
