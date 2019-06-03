package com.smasher.media.adapter;


import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.smasher.media.R;


/**
 * @author matao
 * @date 2019/5/24
 */
public class MusicListAdapter extends BaseRecyclerViewAdapter<MediaItem, MusicViewHolder> {

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
