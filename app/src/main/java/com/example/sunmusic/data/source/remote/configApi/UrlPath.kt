package com.example.sunmusic.data.source.remote.configApi

object UrlPath {
    const val ALBUMS = "albums"
    const val TRACKS = "tracks"
    const val IMAGES = "images"
    const val TOP = "top"
    const val ARTISTS = "artists"

    fun getLimitParam(limit: Int) = "limit=$limit"
    fun getOffsetParam(offset: Int) = "offset=$offset"
    fun getTopAlbumRouter() = "$ALBUMS/$TOP"
    fun getTopTrackRouter() = "$TRACKS/$TOP"
    fun getImageAlbumRouter(albumId: String) = "$ALBUMS/$albumId/$IMAGES"
    fun getImageTrackRouter(artistsId: String) = "$ARTISTS/$artistsId/$IMAGES"
}
