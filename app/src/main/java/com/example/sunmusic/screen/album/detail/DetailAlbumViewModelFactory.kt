package com.example.sunmusic.screen.album.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sunmusic.data.repository.AlbumRepository
import com.example.sunmusic.data.repository.AlbumRepositoryImpl
import com.example.sunmusic.data.repository.TrackRepository
import com.example.sunmusic.data.repository.TrackRepositoryImpl

class DetailAlbumViewModelFactory(private val albumId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            TrackRepository::class.java,
            AlbumRepository::class.java,
            String::class.java
        ).newInstance(
            TrackRepositoryImpl.instance,
            AlbumRepositoryImpl.instance,
            albumId
        )
    }
}