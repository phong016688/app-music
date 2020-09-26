package com.example.sunmusic.screen.album.detail

import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.model.Track

data class DetailAlbumState(
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val isLoad: Boolean = false,
    val error: String = ""
){
    companion object{
        fun initial() = DetailAlbumState(isLoad = true)
    }
}
