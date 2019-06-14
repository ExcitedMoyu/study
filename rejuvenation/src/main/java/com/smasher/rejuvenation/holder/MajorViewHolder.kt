package com.smasher.rejuvenation.holder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.smasher.rejuvenation.MajorData
import com.smasher.rejuvenation.R
import com.smasher.widget.base.BaseRecyclerViewHolder

/**
 * @author matao
 * @date 2019/6/14
 */
class MajorViewHolder(itemView: View) : BaseRecyclerViewHolder<MajorData>(itemView) {
    var cover = itemView.findViewById<ImageView>(R.id.book_cover)
    var name = itemView.findViewById<TextView>(R.id.book_name)
    var author = itemView.findViewById<TextView>(R.id.author)
    var info = itemView.findViewById<TextView>(R.id.info)
    var description = itemView.findViewById<TextView>(R.id.description)

    init {
//        val title = itemView.findViewById<ImageView>(R.id.book_cover)
//        val name = itemView.findViewById<TextView>(R.id.book_name)
//        val author = itemView.findViewById<TextView>(R.id.author)
//        val info = itemView.findViewById<TextView>(R.id.info)
//        val description = itemView.findViewById<TextView>(R.id.description)
    }

    override fun bindView() {
        super.bindView()
        Log.d(TAG, mItem.image.toString())
        Glide.with(mContext).load(mItem.image).into(cover)
        name.text = "圣墟"
        author.text = "辰东"
        info.text = "· 玄幻· 连载"
        description.text = "在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角……"
    }

    companion object {
        private const val TAG = "Holder"
    }

}