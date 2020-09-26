package com.example.sunmusic.screen.play

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.data.model.PlayMusic
import com.example.sunmusic.screen.main.MainActivity
import kotlinx.android.synthetic.main.fragment_play_list.view.*

class PlayListFragment : Fragment(R.layout.fragment_play_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PlayListAdapter()
        view.playListRecyclerView.adapter = adapter
        val listMusic = (activity as MainActivity).getMusicService()?.musicPlayer?.listMusic
            ?: emptyList<PlayMusic>()
        adapter.submitList(listMusic)
    }

    companion object {
        fun newInstance() = PlayListFragment()
    }
}