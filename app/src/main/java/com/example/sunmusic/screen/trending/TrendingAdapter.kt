package com.example.sunmusic.screen.trending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunmusic.R
import com.example.sunmusic.utils.BaseViewHolder
import com.example.sunmusic.utils.loadFromUrl
import kotlinx.android.synthetic.main.item_title.view.*
import kotlinx.android.synthetic.main.item_top_albums.view.*
import kotlinx.android.synthetic.main.item_track.view.*
import kotlinx.android.synthetic.main.layout_album.view.*

class TrendingAdapter : RecyclerView.Adapter<BaseViewHolder<TrendingItem>>() {
    private val listItem = mutableListOf<TrendingItem>()

    override fun getItemViewType(position: Int) = listItem[position].type

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<TrendingItem> {
        val inflate = LayoutInflater.from(parent.context)
        return when (viewType) {
            TrendingItem.TYPE_TOP_ALBUMS -> TopAlbumsViewHolder(
                inflate.inflate(R.layout.item_top_albums, parent, false)
            )
            TrendingItem.TYPE_TITLE_TRACK -> TitleTrackViewHolder(
                inflate.inflate(R.layout.item_title, parent, false)
            )
            else -> TrackViewHolder(
                inflate.inflate(R.layout.item_track, parent, false)
            )
        }
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: BaseViewHolder<TrendingItem>, position: Int) {
        holder.bind(listItem[position])
    }

    fun updateListItem(vararg items: TrendingItem) {
        val oldSizeListItem = listItem.size
        listItem.addAll(items)
        listItem.sortBy { it.type }
        if (listItem.size > 0) {
            notifyItemRangeInserted(oldSizeListItem, items.size)
        } else {
            listItem.clear()
            notifyDataSetChanged()
        }
    }

    class TopAlbumsViewHolder(itemView: View) : BaseViewHolder<TrendingItem>(itemView) {

        override fun bind(item: TrendingItem) = with(itemView) {
            if (item !is TrendingItem.AlbumsItem) return
            albumTop1Layout.run {
                avatarImageView.loadFromUrl("")
                nameTextView.text = item.listAlbum[0].name
                oderTextView.text = context.getString(R.string.number_1)
            }
            albumTop2Layout.run {
                avatarImageView.loadFromUrl("")
                nameTextView.text = item.listAlbum[1].name
                oderTextView.text = context.getString(R.string.number_2)
            }
            albumTop3Layout.run {
                avatarImageView.loadFromUrl("")
                nameTextView.text = item.listAlbum[2].name
                oderTextView.text = context.getString(R.string.number_3)
            }
        }
    }

    class TrackViewHolder(itemView: View) : BaseViewHolder<TrendingItem>(itemView) {

        override fun bind(item: TrendingItem) = with(itemView) {
            if (item !is TrendingItem.TrackItem) return
            trackImageView.loadFromUrl("")
            nameTrackTextView.text = item.track.name
            descripsionTrackTextView.text = item.track.artistName
            oderTrackTextView.text = item.position.toString()
        }
    }

    class TitleTrackViewHolder(itemView: View) : BaseViewHolder<TrendingItem>(itemView) {

        override fun bind(item: TrendingItem) = with(itemView) {
            titleTextView.text = context.getString(R.string.list_track)
        }
    }
}
