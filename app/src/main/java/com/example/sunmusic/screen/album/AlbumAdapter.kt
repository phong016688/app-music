package com.example.sunmusic.screen.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sunmusic.R
import com.example.sunmusic.data.model.Album
import com.example.sunmusic.utils.BaseViewHolder
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.LoadMoreViewHolder
import com.example.sunmusic.utils.loadFromUrl
import kotlinx.android.synthetic.main.album_item.view.*
import kotlinx.android.synthetic.main.item_load_more.view.*

sealed class AlbumNewItem(val type: Int) {
    class AlbumItem(val album: Album) : AlbumNewItem(ALBUM_ITEM_TYPE)
    class LoadMoreItem(var isShowLoad: Boolean) : AlbumNewItem(LOAD_MORE_TYPE)

    companion object {
        const val ALBUM_ITEM_TYPE = 0
        const val LOAD_MORE_TYPE = 1
    }
}

class AlbumAdapter : RecyclerView.Adapter<BaseViewHolder<AlbumNewItem>>() {
    private val listData = mutableListOf<AlbumNewItem>(AlbumNewItem.LoadMoreItem(true))

    override fun getItemViewType(position: Int) = listData[position].type

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AlbumNewItem> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            AlbumNewItem.LOAD_MORE_TYPE -> AlbumLoadMoreViewHolder(
                inflater.inflate(R.layout.item_load_more, parent, false)
            )
            else -> AlbumViewHolder(
                inflater.inflate(R.layout.album_item, parent, false)
            )
        }
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: BaseViewHolder<AlbumNewItem>, position: Int) {
        holder.bind(listData[position])
    }

    fun clearData() {
        listData.clear()
        notifyDataSetChanged()
        listData.add(AlbumNewItem.LoadMoreItem(true))
    }

    fun updateData(albums: List<Album>) {
        val itemsAdd = albums.filterNot { item ->
            listData.any { it is AlbumNewItem.AlbumItem && it.album.id == item.id }
        }
        if (itemsAdd.isEmpty()) return
        listData.addAll(listData.size - 1, itemsAdd.map { AlbumNewItem.AlbumItem(it) })
        notifyDataSetChanged()
    }

    fun startLoadMore() {
        val itemLast = listData.last()
        if (itemLast is AlbumNewItem.LoadMoreItem && !itemLast.isShowLoad) {
            itemLast.isShowLoad = true
            notifyItemChanged(itemCount - 1)
        }
    }

    fun stopLoadMore() {
        val itemLast = listData.last()
        if (itemLast is AlbumNewItem.LoadMoreItem && itemLast.isShowLoad) {
            itemLast.isShowLoad = false
            notifyItemChanged(itemCount - 1)
        }
    }

    fun isLoadMore() = (listData.last() as? AlbumNewItem.LoadMoreItem)?.isShowLoad ?: false

    class AlbumViewHolder(itemView: View) : BaseViewHolder<AlbumNewItem>(itemView) {

        init {
            itemView.setOnClickListener(null)
        }

        override fun bind(item: AlbumNewItem) = with(itemView) {
            if (item is AlbumNewItem.AlbumItem) {
                avatarAlbumImageView.loadFromUrl(item.album.image)
                nameAlbumTextView.text = item.album.name
                nameArtistTextView.text = item.album.artistName
            }
        }
    }

    class AlbumLoadMoreViewHolder(itemView: View) : LoadMoreViewHolder<AlbumNewItem>(itemView) {
        override fun bind(item: AlbumNewItem) {
            if (item is AlbumNewItem.LoadMoreItem) {
                if (item.isShowLoad)
                    itemView.loadMoreItem.visibility = View.VISIBLE
                else
                    itemView.loadMoreItem.visibility = View.GONE
            }
        }
    }
}
