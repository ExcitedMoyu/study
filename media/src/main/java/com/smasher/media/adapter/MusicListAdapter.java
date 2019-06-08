package com.smasher.media.adapter;


import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smasher.media.R;
import com.smasher.widget.base.BaseRecyclerViewAdapter;


/**
 * @author matao
 * @date 2019/5/24
 */
public class MusicListAdapter extends BaseRecyclerViewAdapter<MediaSessionCompat.QueueItem, MusicViewHolder> {

    public MusicListAdapter(Context context) {
        super(context);
    }

    @Override
    public MusicViewHolder onCreateDefineViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music, viewGroup, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
