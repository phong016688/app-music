package com.example.sunmusic.screen.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.repository.AlbumRepository
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.subscribe

class AlbumViewModel(
    private val albumRepository: AlbumRepository
) : ViewModel() {
    private val _liveData = MutableLiveData<AlbumNewState>()
    val liveData: LiveData<AlbumNewState> = _liveData

    init {
        loadAlbumsNew()
    }

    private fun loadAlbumsNew() {
        _liveData.value = AlbumNewState.load()
        albumRepository.getNewAlbums(Constant.DEFAULT_FIRST_LOAD_COUNT, Constant.FIRST_ITEM_INDEX)
            .subscribe({
                _liveData.value = liveData.value?.copy(
                    isLoad = false,
                    albums = it + (liveData.value?.albums ?: emptyList())
                )
            }, {
                _liveData.value = liveData.value?.copy(isLoad = false, throwable = it)
            })
    }

    fun loadMore() {
        _liveData.value = liveData.value?.copy(isLoad = true)
        albumRepository.getNewAlbums(Constant.LOAD_MORE_COUNT, liveData.value?.albums?.size ?: 0)
            .subscribe({
                _liveData.value = liveData.value?.copy(
                    isLoad = false,
                    albums = it + (liveData.value?.albums ?: emptyList())
                )
            }, {
                _liveData.value = liveData.value?.copy(isLoad = false, throwable = it)
            })
    }
}