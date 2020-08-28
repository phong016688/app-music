package com.example.sunmusic.data.repository

import com.example.sunmusic.data.model.Track
import com.example.sunmusic.data.source.TrackDataSource
import com.example.sunmusic.data.source.remote.TrackRemoteDataSource
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface TrackRepository {
    fun getTopTracks(limit: Int): List<Track>
}

class TrackRepositoryImpl(private val remote: TrackDataSource.Remote) : TrackRepository {
    companion object {
        val instance by lazy(SYNCHRONIZED) { TrackRepositoryImpl(TrackRemoteDataSource.instance) }
    }

    override fun getTopTracks(limit: Int): List<Track> {
        return remote.getTopTracks(limit).map { it.toTrack() }
    }
}
