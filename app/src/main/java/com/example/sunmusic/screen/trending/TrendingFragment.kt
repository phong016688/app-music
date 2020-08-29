package com.example.sunmusic.screen.trending

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.data.repository.AlbumRepositoryImpl
import com.example.sunmusic.data.repository.TrackRepositoryImpl
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
        }
        adapter.updateListItem(TrendingItem.TitleTrack)
    }

    override fun showTopAlbums(albums: List<Album>) {
        adapter.updateListItem(TrendingItem.AlbumsItem(albums))
    }

    override fun showTopTracks(tracks: List<Track>) {
        val trackItems = tracks.mapIndexed { index, track -> TrendingItem.TrackItem(track, index) }
        adapter.updateListItem(*trackItems.toTypedArray())
    }

    override fun showError(throwable: Throwable) {
        context?.toast(throwable.message.toString())
    }

    override fun onDestroy() {
        presenter.onStop()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = TrendingFragment()
    }
}
