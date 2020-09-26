package com.example.sunmusic.screen.album.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.sunmusic.R
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.utils.BaseViewHolder
import com.example.sunmusic.utils.loadFromUrl
import kotlinx.android.synthetic.main.item_track.view.*

class TracksInAlbumDiffUtil : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Track, newItem: Track) = oldItem == newItem

    companion object {
        val instance by lazy { TracksInAlbumDiffUtil() }
    }
}

class TracksInAlbumAdapter :
    ListAdapter<Track, TracksInAlbumAdapter.TrackViewHolder>(TracksInAlbumDiffUtil.instance) {
    private var trackClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false),
            trackClickListener
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setTrackClickListener(trackClickListener: ((Track) -> Unit)?) {
        this.trackClickListener = trackClickListener
    }

    class TrackViewHolder(
        itemView: View,
        private val itemClickListener: ((Track) -> Unit)?
    ) : BaseViewHolder<Track>(itemView) {
        private var track: Track? = null

        init {
            itemView.setOnClickListener {
                track?.let { itemClickListener?.invoke(it) }
            }
        }

        override fun bind(item: Track) = with(itemView) {
            this@TrackViewHolder.track = item
            trackImageView.loadFromUrl(item.image)
            nameTrackTextView.text = item.name
            descripsionTrackTextView.text = item.artistName
            oderTrackTextView.visibility = View.INVISIBLE
            playButtonImageView.visibility = View.INVISIBLE
        }
    }
}
