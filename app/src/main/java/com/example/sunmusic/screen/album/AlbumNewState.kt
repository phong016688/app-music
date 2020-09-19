package com.example.sunmusic.screen.album

import com.example.sunmusic.data.model.Album

data class AlbumNewState(
    val isLoad: Boolean,
    val throwable: Throwable?,
    val albums: List<Album>
) {
    companion object {
        fun load() = AlbumNewState(
            true,
            null,
            emptyList()
        )

        fun initial() = AlbumNewState(
            false,
            null,
            emptyList()
        )
    }
}
