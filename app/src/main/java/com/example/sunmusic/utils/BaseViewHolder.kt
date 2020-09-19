package com.example.sunmusic.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.sunmusic.R
import com.example.sunmusic.screen.trending.TrendingItem

abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}

open class LoadMoreViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView) {

    override fun bind(item: T) = Unit
}
