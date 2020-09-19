package com.example.sunmusic.screen.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sunmusic.data.repository.AlbumRepository
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.subscribe

class AlbumViewModel(
    private val albumRepository: AlbumRepository
) : ViewModel() {
    private val _liveData = MutableLiveData<AlbumNewState>()
    val liveData: LiveData<AlbumNewState> = _liveData
    private var currentOffset = Constant.DEFAULT_FIRST_LOAD_COUNT

    init {
        loadAlbumsNew()
    }

    private fun loadAlbumsNew() {
        _liveData.value = AlbumNewState.load()
        albumRepository.getNewAlbums(Constant.DEFAULT_FIRST_LOAD_COUNT, Constant.FIRST_ITEM_INDEX)
            .subscribe({
                _liveData.value = AlbumNewState(false, null, it)
            }, {
                _liveData.value = AlbumNewState(false, it, emptyList())
            })
    }

    fun loadMore() {
        _liveData.value = AlbumNewState.load()
        albumRepository.getNewAlbums(Constant.LOAD_MORE_COUNT, currentOffset)
            .subscribe({
                _liveData.value = AlbumNewState(false, null, it)
                currentOffset += Constant.LOAD_MORE_COUNT
            }, {
                _liveData.value = AlbumNewState(false, it, emptyList())
            })
    }
}