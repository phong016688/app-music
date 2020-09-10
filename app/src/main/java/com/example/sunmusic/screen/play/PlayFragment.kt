package com.example.sunmusic.screen.play

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.screen.main.MainActivity
import com.example.sunmusic.utils.loadFromUrl
import com.example.sunmusic.utils.musicplayer.MusicMediaPlayer
import com.example.sunmusic.utils.musicplayer.StateMusic
import kotlinx.android.synthetic.main.fragment_play.view.*
import kotlinx.android.synthetic.main.layout_bind_music.view.*

class PlayFragment : Fragment(R.layout.fragment_play), MusicMediaPlayer.MediaListener {
    private var musicPlayer: MusicMediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.getMusicService()?.musicPlayer?.also {
            it.startMusic()
            musicPlayer = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_alpha)
        view.imageMusicLayout.startAnimation(animation)
        musicPlayer?.addOnStateChangeListener(this)
    }

    override fun onDestroyView() {
        musicPlayer?.removeOnStateChangeListener(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        musicPlayer = null
        super.onDestroy()
    }

    override fun onStateChange(state: StateMusic) {
        when (state) {
            StateMusic.STARTED -> {
                setEnabledAction(true)
                updateBackgroundPlayButton(true)
            }
            StateMusic.PAUSED -> {
                updateBackgroundPlayButton(false)
            }
            StateMusic.PREPARING -> {
                showInfoMusic()
                setEnabledAction(false)
                updateBackgroundPlayButton(false)
            }
            StateMusic.COMPLETED -> {
                setEnabledAction(false)
                updateBackgroundPlayButton(false)
            }
            else -> Unit
        }
    }

    private fun showInfoMusic() = with(requireView()) {
        val currentPlayMusic = musicPlayer?.currentPlayMusic() ?: return
        binMusicLayout.nameListeningTextView.text = currentPlayMusic.name
        nameMusicTextView.text = currentPlayMusic.name
        artistNameTextView.text = currentPlayMusic.artistName
        endTimeTextView.text = currentPlayMusic.duration.toString()
        musicIconImageView.loadFromUrl(currentPlayMusic.image)
    }

    private fun setEnabledAction(isEnable: Boolean) = with(requireView().binMusicLayout) {
        playImageView.isEnabled = isEnable
        backImageView.isEnabled = isEnable
        nextImageView.isEnabled = isEnable
    }

    private fun updateBackgroundPlayButton(isPlay: Boolean) {
        requireView().binMusicLayout.playImageView.setBackgroundResource(
            if (isPlay)
                R.drawable.ic_baseline_pause_circle_outline_24
            else
                R.drawable.ic_baseline_play_circle_outline_24
        )
    }

    companion object {
        fun newInstance() = PlayFragment()
    }
}
