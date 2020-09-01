package com.example.sunmusic.data.source.remote

import com.example.sunmusic.data.source.TrackDataSource
import com.example.sunmusic.data.source.remote.configApi.ApiService
import com.example.sunmusic.data.source.remote.configApi.ApiServiceImpl
import com.example.sunmusic.data.source.remote.configApi.UrlPath
import com.example.sunmusic.data.source.remote.response.ImageResponse
import com.example.sunmusic.data.source.remote.response.TrackResponse
import com.example.sunmusic.utils.Error
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

class TrackRemoteDataSource(private val apiService: ApiService) : TrackDataSource.Remote {

    companion object {
        val instance by lazy(SYNCHRONIZED) { TrackRemoteDataSource(ApiServiceImpl.instance) }
    }

    override fun getTopTracks(limit: Int): List<TrackResponse> {
        val data = apiService.get(
            UrlPath.TRACKS,
            TrackResponse::class.java,
            UrlPath.getTopTrackRouter(),
            UrlPath.getLimitParam(limit)
        )
        if (data.isNotEmpty()) {
            return data
        }
        throw Throwable(Error.NO_DATA)
    }

    override fun getImageTrack(artistsId: String): ImageResponse {
        val data = apiService.get(
            UrlPath.IMAGES,
            ImageResponse::class.java,
            UrlPath.getImageTrackRouter(artistsId)
        )
        return data.firstOrNull() ?: ImageResponse()
    }
}
