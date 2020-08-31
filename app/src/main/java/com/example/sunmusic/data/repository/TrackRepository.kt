package com.example.sunmusic.data.repository

import com.example.sunmusic.data.executor.AppExecutor
import com.example.sunmusic.data.executor.AppExecutorImpl
import com.example.sunmusic.data.model.Track
import com.example.sunmusic.data.source.TrackDataSource
import com.example.sunmusic.data.source.remote.TrackRemoteDataSource
import java.util.concurrent.Future
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface TrackRepository {
    fun getTopTracks(limit: Int): Future<List<Track>>
}

class TrackRepositoryImpl(
    private val remote: TrackDataSource.Remote,
    private val appExecutor: AppExecutor
) : TrackRepository {
    companion object {
        val instance by lazy(SYNCHRONIZED) {
            TrackRepositoryImpl(
                TrackRemoteDataSource.instance,
                AppExecutorImpl.instance
            )
        }
    }

    override fun getTopTracks(limit: Int): Future<List<Track>> {
        return appExecutor.create { remote.getTopTracks(limit).map { it.toTrack() } }
    }
}
