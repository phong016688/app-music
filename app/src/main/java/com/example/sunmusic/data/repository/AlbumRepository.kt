package com.example.sunmusic.data.repository

import com.example.sunmusic.data.executor.AppExecutor
import com.example.sunmusic.data.executor.AppExecutorImpl
import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.source.AlbumDataSource
import com.example.sunmusic.data.source.remote.AlbumRemoteDataSource
import java.util.concurrent.Future
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface AlbumRepository {
    fun getTopAlbums(limit: Int): Future<List<Album>>
    fun getNewAlbums(limit: Int, offset: Int): Future<List<Album>>
    fun getDetailAlbum(albumId: String): Future<Album>
}

class AlbumRepositoryImpl(
    private val remote: AlbumDataSource.Remote,
    private val appExecutor: AppExecutor
) : AlbumRepository {
    companion object {
        val instance by lazy(SYNCHRONIZED) {
            AlbumRepositoryImpl(
                AlbumRemoteDataSource.instance,
                AppExecutorImpl.instance
            )
        }
    }

    override fun getTopAlbums(limit: Int): Future<List<Album>> {
        return appExecutor.create {
            remote.getTopAlbums(limit).map {
                val imageResponse = remote.getImageAlbum(it.id)
                it.toAlbum(imageResponse.url)
            }
        }
    }

    override fun getNewAlbums(limit: Int, offset: Int): Future<List<Album>> {
        return appExecutor.create {
            remote.getNewAlbums(limit, offset).map {
                val imageResponse = remote.getImageAlbum(it.id)
                it.toAlbum(imageResponse.url)
            }
        }
    }

    override fun getDetailAlbum(albumId: String): Future<Album> {
        return appExecutor.create {
            val detailAlbum = remote.getDetailAlbum(albumId)
            val imageResponse = remote.getImageAlbum(detailAlbum.id)
            detailAlbum.toAlbum(imageResponse.url)
        }
    }
}
