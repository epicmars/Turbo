package com.androidpi.turbo.ui.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidpi.turbo.R

/**
 * Created on 2019-12-12.
 */
class HomeEntryViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var ivIcon: ImageView? = null
    private var tvName: TextView? = null

    init {
        ivIcon = itemView.findViewById(R.id.iv_icon)
        tvName = itemView.findViewById(R.id.tv_name)
    }

    fun setName(name: CharSequence?) {
        tvName?.setText(name)
    }

    fun setIcon(icon: Int) {
        ivIcon?.setImageResource(icon)
    }

    fun setIcon(icon: Drawable?) {
        ivIcon?.setImageDrawable(icon)
    }
}