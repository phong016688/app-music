package com.example.sunmusic.screen.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sunmusic.data.repository.AlbumRepository

class AlbumViewModelFactory(
    private val albumRepository: AlbumRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AlbumRepository::class.java).newInstance(albumRepository)
    }
}
