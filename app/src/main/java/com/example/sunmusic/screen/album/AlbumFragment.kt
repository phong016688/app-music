package com.example.sunmusic.screen.album

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunmusic.R
import com.example.sunmusic.data.repository.AlbumRepositoryImpl
import com.example.sunmusic.screen.album.detail.DetailAlbumFragment
import com.example.sunmusic.utils.CustomOnScrollListener
import com.example.sunmusic.utils.replaceFragment
import com.example.sunmusic.utils.toast
import kotlinx.android.synthetic.main.fragment_album.*
import kotlinx.android.synthetic.main.fragment_album.view.*

class AlbumFragment : Fragment(R.layout.fragment_album) {
    private lateinit var albumViewModel: AlbumViewModel
    private var onScrollListener: CustomOnScrollListener? = null
    private val albumNewAdapter by lazy { AlbumAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = AlbumViewModelFactory(AlbumRepositoryImpl.instance)
        albumViewModel = ViewModelProvider(this, factory).get(AlbumViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        handleEvents()
        observeView()
    }

    private fun handleEvents() {
        albumNewAdapter.setItemClick {
            when (it) {
                is AlbumNewItem.AlbumItem -> activity?.replaceFragment(
                    DetailAlbumFragment.newInstance(it.album.id),
                    R.id.container,
                    true
                )
            }
        }
    }

    private fun setupRecyclerView() = view?.let {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val isItemLoadMore = albumNewAdapter.run { position == itemCount - 1 }
                return if (isItemLoadMore) 2 else 1
            }
        }
        with(albumNewRecyclerView) {
            layoutManager = gridLayoutManager
            adapter = this@AlbumFragment.albumNewAdapter
            itemAnimator = DefaultItemAnimator()
            hasFixedSize()
            onScrollListener = createOnScrollListener(layoutManager)
            addOnScrollListener(onScrollListener ?: return@let)
            this@AlbumFragment.albumNewAdapter.clearData()
        }
    }

    private fun createOnScrollListener(layoutManager: RecyclerView.LayoutManager?) =
        layoutManager?.let { CustomOnScrollListener(it) { albumViewModel.loadMore() } }

    override fun onDestroyView() {
        onScrollListener?.let { view?.albumNewRecyclerView?.removeOnScrollListener(it) }
        super.onDestroyView()
    }

    private fun observeView() {
        albumViewModel.liveData.observe(this, Observer {
            when {
                it.isLoad -> albumNewAdapter.startLoadMore()
                it.throwable != null -> context?.toast(it.throwable.message.toString())
                else -> {
                    albumNewAdapter.updateData(it.albums)
                    albumNewAdapter.stopLoadMore()
                }
            }
        })
    }

    companion object {
        fun newInstance() = AlbumFragment()
    }
}
