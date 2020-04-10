package com.smasher.rejuvenation.holder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smasher.rejuvenation.R

/**
 * @author matao
 * @date 2019/6/14
 */
class MajorCommonViewHolder(itemView: View) : BaseMajorViewHolder(itemView) {

    private var cover = itemView.findViewById<ImageView>(R.id.book_cover)
    private var name = itemView.findViewById<TextView>(R.id.book_name)
    private var author = itemView.findViewById<TextView>(R.id.author)
    private var info = itemView.findViewById<TextView>(R.id.info)
    private var description = itemView.findViewById<TextView>(R.id.description)

    init {
        Log.d("TAG", "MajorCommonViewHolder")
    }

    override fun bindView() {
        super.bindView()
        Log.d(TAG, mItem.image.toString())
        name.text = "圣墟"
        author.text = "辰东"
        info.text = "· 玄幻· 连载"
        description.text = "在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角……"
        Glide.with(mContext).load(mItem.image).into(cover)
    }

    companion object {
        private const val TAG = "Holder"
    }

}