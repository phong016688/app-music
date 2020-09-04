package com.example.sunmusic.screen.trending

import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.utils.BasePresenter
import com.example.sunmusic.utils.Constant

interface TrendingContract {
    interface View {
        fun showTopAlbums(albums: List<Album>)
        fun showTopTracks(tracks: List<Track>)
        fun showError(throwable: Throwable)
    }

    interface Presenter : BasePresenter<View> {
        fun getTopAlbums(limit: Int = Constant.DEFAULT_TOP_ALBUM_COUNT)
        fun getTopTracks(limit: Int = Constant.DEFAULT_TOP_TRACK_COUNT)
        fun loadMoreTopTracks(limit: Int = Constant.LOAD_MORE_COUNT, offset: Int)
    }
}
