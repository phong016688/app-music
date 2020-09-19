package com.example.sunmusic.data.source

import com.example.sunmusic.data.source.remote.response.AlbumResponse
import com.example.sunmusic.data.source.remote.response.ImageResponse

interface AlbumDataSource {
    interface Remote {
        fun getTopAlbums(limit: Int): List<AlbumResponse>
        fun getImageAlbum(albumId: String): ImageResponse
        fun getNewAlbums(limit: Int, offset: Int): List<AlbumResponse>
        fun getDetailAlbum(albumId: String)
    }
}
