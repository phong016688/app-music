package com.example.sunmusic.screen.album.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunmusic.data.repository.AlbumRepository
import com.example.sunmusic.data.repository.TrackRepository
import com.example.sunmusic.utils.subscribe

class DetailAlbumViewModel(
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
    albumId: String
) : ViewModel() {
    private val _liveData = MutableLiveData<DetailAlbumState>()
    val liveData = _liveData

    init {
        _liveData.value = DetailAlbumState.initial()
        getDetailAlbum(albumId)
        getTracksInAlbum(albumId)
    }

    private fun getDetailAlbum(albumId: String) {
        albumRepository.getDetailAlbum(albumId).subscribe({
            _liveData.value = liveData.value?.copy(album = it, isLoad = true)
        }, {
            _liveData.value = liveData.value?.copy(
                error = it.message.toString(),
                isLoad = false
            )
        })
    }

    private fun getTracksInAlbum(albumId: String) {
        trackRepository.getTracksInAlbum(albumId).subscribe({
            _liveData.value = liveData.value?.copy(tracks = it, isLoad = false)
        }, {
            _liveData.value = liveData.value?.copy(
                error = it.message.toString(),
                isLoad = false
            )
        })
    }

}