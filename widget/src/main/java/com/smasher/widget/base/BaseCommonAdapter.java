package com.smasher.widget.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moyu
 */
public abstract class BaseCommonAdapter<T, VH extends BaseCommonViewHolder<T>> extends BaseAdapter {


    private LayoutInflater mInflater;

    private List<T> mList;

    public BaseCommonAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<T> list) {
        if (list == null) {
            mList = new ArrayList<>();
        } else {
            mList = list;
        }
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder = null;
        if (convertView != null) {
            holder = (VH) convertView.getTag();
        } else {
            convertView = createConvertView(mInflater, parent);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.setItem(getItem(position));
        holder.bindView();
        return convertView;
    }


    public abstract View createConvertView(LayoutInflater inflater, ViewGroup parent);

    public abstract VH createViewHolder(View convertView);
}
