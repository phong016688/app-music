package com.example.sunmusic.data.source

import com.example.sunmusic.data.source.remote.response.AlbumResponse

interface AlbumDataSource {
    interface Remote {
        fun getTopAlbums(limit: Int): List<AlbumResponse>
    }
}
