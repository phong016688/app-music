package com.example.sunmusic.data.repository

import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.source.AlbumDataSource
import com.example.sunmusic.data.source.remote.AlbumRemoteDataSource
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface AlbumRepository {
    fun getTopAlbums(limit: Int): List<Album>
}

class AlbumRepositoryImpl(private val remote: AlbumDataSource.Remote) : AlbumRepository {
    companion object {
        val instance by lazy(SYNCHRONIZED) { AlbumRepositoryImpl(AlbumRemoteDataSource.instance) }
    }

    override fun getTopAlbums(limit: Int): List<Album> {
        return remote.getTopAlbums(limit).map { it.toAlbum() }
    }
}
