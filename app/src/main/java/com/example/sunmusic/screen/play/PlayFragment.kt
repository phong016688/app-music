package com.example.sunmusic.screen.play

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import kotlinx.android.synthetic.main.fragment_play.view.*

class PlayFragment : Fragment(R.layout.fragment_play) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_alpha)
        view.imageMusicLayout.startAnimation(animation)
    }

    companion object {
        fun newInstance() = PlayFragment()
    }
}
