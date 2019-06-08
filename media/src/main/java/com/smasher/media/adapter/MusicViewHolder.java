package com.smasher.media.adapter;

import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.smasher.media.R;
import com.smasher.widget.base.BaseRecyclerViewHolder;

/**
 * @author matao
 * @date 2019/5/24
 */
public class MusicViewHolder extends BaseRecyclerViewHolder<MediaSessionCompat.QueueItem> {

    ImageView mMusicIcon;
    TextView mMusicName;

    MusicViewHolder(@NonNull View itemView) {
        super(itemView);
        mMusicIcon = itemView.findViewById(R.id.music_icon);
        mMusicName = itemView.findViewById(R.id.music_name);
    }


    @Override
    public void bindView() {
        super.bindView();
        if (mItem != null) {
            MediaDescriptionCompat description = mItem.getDescription();
            mMusicName.setText(description.getTitle());
        }
    }
}
