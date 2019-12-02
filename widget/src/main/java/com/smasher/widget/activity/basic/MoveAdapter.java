package com.smasher.widget.activity.basic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smasher.widget.R;
import com.smasher.widget.base.BaseRecyclerViewAdapter;
import com.smasher.widget.base.IOperationData;

import java.util.Collections;

/**
 * @author matao
 * @date 2019/6/10
 */
public class MoveAdapter extends BaseRecyclerViewAdapter<Integer, MoveViewHolder> implements IOperationData {


    public MoveAdapter(Context context) {
        super(context);
    }


    @Override
    public MoveViewHolder onCreateDefineViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_text, viewGroup, false);
        return new MoveViewHolder(view);
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

}
