package com.example.sunmusic.screen.trending

import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.model.Track

sealed class TrendingItem(val type: Int) {
    data class AlbumsItem(val listAlbum: List<Album>) : TrendingItem(TYPE_TOP_ALBUMS)
    data class TrackItem(val track: Track, val position: Int) : TrendingItem(TYPE_TRACK_VIEW)
    object TitleTrack : TrendingItem(TYPE_TITLE_TRACK)

    companion object {
        const val TYPE_TOP_ALBUMS = 0
        const val TYPE_TITLE_TRACK = 1
        const val TYPE_TRACK_VIEW = 2
    }
}
