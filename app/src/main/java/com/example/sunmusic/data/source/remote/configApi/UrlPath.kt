package com.example.sunmusic.data.source.remote.configApi

object UrlPath {
    const val ALBUM_ROUTER = "albums/new"
    const val TRACK_ROUTER = "tracks/top"
    const val ALBUMS = "albums"
    const val TRACKS = "tracks"

    fun getLimitParam(limit: Int) = "limit=$limit"
}
