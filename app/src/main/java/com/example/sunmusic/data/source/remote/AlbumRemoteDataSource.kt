package com.example.sunmusic.data.source.remote

import com.example.sunmusic.data.source.AlbumDataSource
import com.example.sunmusic.data.source.remote.configApi.ApiService
import com.example.sunmusic.data.source.remote.configApi.ApiServiceImpl
import com.example.sunmusic.data.source.remote.configApi.UrlPath
import com.example.sunmusic.data.source.remote.response.AlbumResponse
import com.example.sunmusic.utils.Error
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

class AlbumRemoteDataSource(private val apiService: ApiService) : AlbumDataSource.Remote {

    companion object {
        val instance by lazy(SYNCHRONIZED) { AlbumRemoteDataSource(ApiServiceImpl.instance) }
    }

    override fun getTopAlbums(limit: Int): List<AlbumResponse> {
        val data = apiService.get(
            UrlPath.ALBUMS,
            AlbumResponse::class.java,
            UrlPath.ALBUM_ROUTER,
            UrlPath.getLimitParam(limit)
        )
        if (data.isNotEmpty()) {
            return data
        }
        throw Throwable(Error.NO_DATA)
    }
}
