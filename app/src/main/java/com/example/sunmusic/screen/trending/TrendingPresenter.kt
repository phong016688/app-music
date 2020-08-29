package com.example.sunmusic.screen.trending

import com.example.sunmusic.data.repository.AlbumRepository
import com.example.sunmusic.data.repository.TrackRepository
import com.example.sunmusic.utils.subscribe

class TrendingPresenter(
    private val albumRepository: AlbumRepository,
    private val trackRepository: TrackRepository
) : TrendingContract.Presenter {
    private var view: TrendingContract.View? = null

    override fun getTopAlbums(limit: Int) {
        albumRepository.getTopAlbums(limit)
            .subscribe({
                view?.showTopAlbums(it)
            }, {
                view?.showError(it)
            })
    }

    override fun getTopTracks(limit: Int) {
        trackRepository.getTopTracks(limit)
            .subscribe({
                view?.showTopTracks(it)
            }, {
                view?.showError(it)
            })
    }

    override fun onStart() {
        getTopAlbums()
        getTopTracks()
    }

    override fun onStop() {
        view = null
    }

    override fun setView(view: TrendingContract.View?) {
        this.view = view
    }
}
