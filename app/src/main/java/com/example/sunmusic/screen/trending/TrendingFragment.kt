package com.example.sunmusic.screen.trending

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.data.repository.AlbumRepositoryImpl
import com.example.sunmusic.data.repository.TrackRepositoryImpl
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.CustomOnScrollListener
import com.example.sunmusic.utils.toast
import kotlinx.android.synthetic.main.fragment_trending.view.*

class TrendingFragment : Fragment(R.layout.fragment_trending), TrendingContract.View {
    private val adapter by lazy { TrendingAdapter() }
    private val presenter by lazy {
        TrendingPresenter(
            AlbumRepositoryImpl.instance,
            TrackRepositoryImpl.instance
        )
    }
    private lateinit var onScrollListener: CustomOnScrollListener
    private var currentOffsetTrack = Constant.DEFAULT_TOP_TRACK_COUNT
    private var currentOderTrack = Constant.FIRST_POSITION_INDEX

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        setupView()
        presenter.onStart()
    }

    private fun setupView() {
        view?.trendingRecyclerView?.apply {
            adapter = this@TrendingFragment.adapter
            hasFixedSize()
            val linearLayoutManager = layoutManager ?: return@apply
            val onLoadMore = {
                this@TrendingFragment.adapter.startLoadMore()
                presenter.loadMoreTopTracks(offset = currentOffsetTrack)
                currentOffsetTrack += Constant.LOAD_MORE_COUNT
            }
            onScrollListener = CustomOnScrollListener(linearLayoutManager, onLoadMore)
            addOnScrollListener(onScrollListener)
        }
        adapter.apply {
            addTitleTrackItem()
            startLoadMore()
        }
    }

    override fun showTopAlbums(albums: List<Album>) {
        adapter.addTopAlbumItem(TrendingItem.AlbumsItem(albums))
    }

    override fun showTopTracks(tracks: List<Track>) {
        val trackItems = tracks.map { TrendingItem.TrackItem(it, currentOderTrack++) }
        adapter.apply {
            stopLoadMore()
            addListTrackItem(trackItems)
        }
    }

    override fun showError(throwable: Throwable) {
        adapter.stopLoadMore()
        context?.toast(throwable.message.toString())
    }

    override fun onDestroy() {
        removeOnScrollListenerRecyclerView()
        presenter.onStop()
        super.onDestroy()
    }

    private fun removeOnScrollListenerRecyclerView() {
        if (::onScrollListener.isInitialized) {
            view?.trendingRecyclerView?.removeOnScrollListener(onScrollListener)
        }
    }

    companion object {
        fun newInstance() = TrendingFragment()
    }
}
