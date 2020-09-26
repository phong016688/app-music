package com.example.sunmusic.screen.trending

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunmusic.R
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.utils.BaseViewHolder
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.LoadMoreViewHolder
import com.example.sunmusic.utils.loadFromUrl
import kotlinx.android.synthetic.main.item_title.view.*
import kotlinx.android.synthetic.main.item_top_albums.view.*
import kotlinx.android.synthetic.main.item_track.view.*
import kotlinx.android.synthetic.main.layout_album.view.*

class TrendingAdapter : RecyclerView.Adapter<BaseViewHolder<TrendingItem>>() {
    private val listItem = mutableListOf<TrendingItem>()
    private var isLoadMore = false
    private var itemTrendingCLickListener: ItemTrendingCLickListener? = null

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
            TrendingItem.TYPE_TRACK_VIEW -> TrackViewHolder(
                inflate.inflate(R.layout.item_track, parent, false),
                itemTrendingCLickListener
            )
            else -> LoadMoreViewHolder(
                inflate.inflate(R.layout.item_load_more, parent, false)
            )
        }
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: BaseViewHolder<TrendingItem>, position: Int) {
        holder.bind(listItem[position])
    }

    fun addTitleTrackItem() {
        if (listItem.any { it is TrendingItem.TitleTrack }) return
        listItem.add(TrendingItem.TitleTrack)
        notifyItemInserted(itemCount)
    }

    fun addTopAlbumItem(topAlbumItem: TrendingItem.AlbumsItem) {
        listItem.add(Constant.FIRST_ITEM_INDEX, topAlbumItem)
        notifyItemInserted(Constant.FIRST_POSITION_INDEX)
    }

    fun addListTrackItem(trackItems: List<TrendingItem.TrackItem>) {
        listItem.addAll(trackItems)
        notifyItemRangeInserted(itemCount + Constant.FIRST_POSITION_INDEX, trackItems.size)
    }

    fun startLoadMore() {
        if (!isLoadMore) {
            isLoadMore = true
            listItem.add(TrendingItem.LoadMore)
            notifyItemInserted(itemCount)
        }
    }

    fun stopLoadMore() {
        if (isLoadMore) {
            isLoadMore = false
            listItem.remove(TrendingItem.LoadMore)
            notifyItemRemoved(itemCount - 1)
        }
    }

    fun setOnClickListener(itemClickListener: ItemTrendingCLickListener?) {
        itemTrendingCLickListener = itemClickListener
    }

    class TopAlbumsViewHolder(itemView: View) : BaseViewHolder<TrendingItem>(itemView) {

        override fun bind(item: TrendingItem) = with(itemView) {
            if (item !is TrendingItem.AlbumsItem) return
            albumTop1Layout.run {
                avatarImageView.loadFromUrl(item.listAlbum[0].image)
                nameTextView.text = item.listAlbum[0].name
                oderTextView.text = context.getString(R.string.number_1)
            }
            albumTop2Layout.run {
                avatarImageView.loadFromUrl(item.listAlbum[1].image)
                nameTextView.text = item.listAlbum[1].name
                oderTextView.text = context.getString(R.string.number_2)
            }
            albumTop3Layout.run {
                avatarImageView.loadFromUrl(item.listAlbum[2].image)
                nameTextView.text = item.listAlbum[2].name
                oderTextView.text = context.getString(R.string.number_3)
            }
        }
    }

    class TrackViewHolder(
        itemView: View,
        private val itemClickListener: ItemTrendingCLickListener?
    ) : BaseViewHolder<TrendingItem>(itemView) {
        private var item: TrendingItem.TrackItem? = null

        init {
            itemView.playButtonImageView.setOnClickListener {
                itemClickListener?.onPlayTrackClick(item?.track ?: return@setOnClickListener)
            }
        }

        override fun bind(item: TrendingItem) = with(itemView) {
            if (item !is TrendingItem.TrackItem) return
            this@TrackViewHolder.item = item
            trackImageView.loadFromUrl(item.track.image)
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

    interface ItemTrendingCLickListener {
        fun onPlayTrackClick(track: Track)
    }
}
