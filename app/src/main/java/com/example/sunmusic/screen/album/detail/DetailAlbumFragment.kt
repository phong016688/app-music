package com.example.sunmusic.screen.album.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunmusic.R
import com.example.sunmusic.data.model.PlayMusic
import com.example.sunmusic.screen.main.MainActivity
import com.example.sunmusic.screen.play.PlayFragment
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.loadFromUrl
import com.example.sunmusic.utils.replaceFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.detail_album_fragment.view.*

class DetailAlbumFragment : Fragment(R.layout.detail_album_fragment) {
    private lateinit var viewModel: DetailAlbumViewModel
    private lateinit var trackInAlbumAdapter: TracksInAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val albumId = arguments?.getString(Constant.ALBUM_ID, "") ?: ""
        val factory = DetailAlbumViewModelFactory(albumId)
        viewModel = ViewModelProvider(this, factory).get(DetailAlbumViewModel::class.java)
        trackInAlbumAdapter = TracksInAlbumAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserve()
        handleEvents()
    }

    private fun handleEvents() {
        view?.playAllFloatButton?.setOnClickListener {
            (activity as MainActivity).apply {
                replaceFragment(PlayFragment.newInstance(), R.id.container, true)
                getMusicService()?.musicPlayer?.addMusics(*trackToPlayMusicArray())
                getMusicService()?.musicPlayer?.nextMusic()
                getMusicService()?.musicPlayer?.startMusic()

            }
        }
        view?.albumToolbar?.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun trackToPlayMusicArray(): Array<out PlayMusic> {
        return (viewModel.liveData.value?.tracks?.map {
            PlayMusic(
                it.id,
                it.name,
                it.previewURL,
                "",
                it.image,
                it.playbackSeconds,
                0,
                it.artistName
            )
        }?.toTypedArray() ?: emptyArray())
    }

    private fun setupObserve() {
        viewModel.liveData.observe(this, Observer {
            if (it.isLoad) {
                view?.progressBar?.visibility = View.VISIBLE
            } else {
                view?.progressBar?.visibility = View.GONE
            }
            if (it.tracks.isNotEmpty()) {
                trackInAlbumAdapter.submitList(it.tracks)
            }
            if (it.error.isNotEmpty()) {
                Snackbar.make(requireView().rootView, it.error, 1000).show()
            }
            if (it.album != null) {
                view?.avatarAlbumImageView?.loadFromUrl(it.album.image)
                view?.albumCollapsingToolbar?.title = it.album.name
            }
        })
    }

    private fun setupView() {
        view?.apply {
            tracksInAlbumRecyclerView.adapter = trackInAlbumAdapter
        }
    }

    companion object {
        fun newInstance(albumId: String) = DetailAlbumFragment().apply {
            arguments = bundleOf(Constant.ALBUM_ID to albumId)
        }
    }
}