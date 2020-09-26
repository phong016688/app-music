package com.example.sunmusic.screen.play

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sunmusic.R
import com.example.sunmusic.screen.main.MainActivity
import com.example.sunmusic.service.DownloadMusicService
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.loadFromUrl
import com.example.sunmusic.utils.musicplayer.MusicMediaPlayer
import com.example.sunmusic.utils.musicplayer.StateMusic
import com.example.sunmusic.utils.replaceFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_play.view.*
import kotlinx.android.synthetic.main.layout_bind_music.view.*
import java.util.concurrent.Executors

class PlayFragment : Fragment(R.layout.fragment_play), MusicMediaPlayer.MediaListener {
    private var musicPlayer: MusicMediaPlayer? = null
    private val executeService = Executors.newFixedThreadPool(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.getMusicService()?.musicPlayer?.also { musicPlayer = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_alpha)
        view.imageMusicLayout.startAnimation(animation)
        musicPlayer?.addOnStateChangeListener(this)
        handleEvents()
        updateSeekBar()
    }

    private fun handleEvents() {
        view?.binMusicLayout?.playImageView?.setOnClickListener {
            if (musicPlayer?.state() == StateMusic.STARTED) {
                musicPlayer?.pauseMusic()
            } else if (musicPlayer?.state() == StateMusic.PAUSED) {
                musicPlayer?.startMusic()
            }
        }
        view?.binMusicLayout?.nextImageView?.setOnClickListener {
            musicPlayer?.pauseMusic()
            musicPlayer?.nextMusic()
            musicPlayer?.startMusic()
        }
        view?.binMusicLayout?.backImageView?.setOnClickListener {
            musicPlayer?.pauseMusic()
            musicPlayer?.backMusic()
            musicPlayer?.startMusic()
        }
        view?.playListTextView?.setOnClickListener {
            activity?.replaceFragment(PlayListFragment.newInstance(), R.id.container, true)
        }
        view?.playImageView?.setOnClickListener {
            activity?.replaceFragment(PlayListFragment.newInstance(), R.id.container, true)
        }
        view?.downloadImageView?.setOnClickListener {
            if (checkPermission()) {
                val currentMusic = musicPlayer?.currentPlayMusic() ?: return@setOnClickListener
                val intent = Intent(requireContext(), DownloadMusicService::class.java)
                intent.putExtra(Constant.DOWNLOAD_URL, currentMusic.url)
                intent.putExtra(Constant.NAME_MUSIC, currentMusic.name)
                activity?.startService(intent)
            } else {
                requestPermission()
            }
        }
    }

    override fun onDestroyView() {
        musicPlayer?.removeOnStateChangeListener(this)
        executeService.shutdown()
        super.onDestroyView()
    }

    override fun onDestroy() {
        musicPlayer = null
        super.onDestroy()
    }

    override fun onStateChange(state: StateMusic) {
        when (state) {
            StateMusic.STARTED -> {
                showInfoMusic()
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
                resetSeekBar()
            }
            StateMusic.COMPLETED -> {
                setEnabledAction(false)
                updateBackgroundPlayButton(false)
            }
            else -> Unit
        }
    }

    private fun updateSeekBar() {
        executeService.execute {
            while (true) {
                Thread.sleep(1000)
                if (musicPlayer?.state() != StateMusic.STARTED) continue
                Handler(Looper.getMainLooper()).post {
                    val duration = musicPlayer?.durationStarted()
                    val text = duration?.let { "${it / 1000 / 60}: ${it / 1000 % 60}" }
                    view?.currentTimeTextView?.text = text
                    view?.musicSeekBar?.progress = duration?.let { it / 1000 } ?: 0
                }
            }
        }
    }

    private fun resetSeekBar() {
        view?.currentTimeTextView?.text = "0:00"
        view?.musicSeekBar?.progress = 0
    }

    private fun showInfoMusic() = with(requireView()) {
        val currentPlayMusic = musicPlayer?.currentPlayMusic() ?: return
        binMusicLayout.nameListeningTextView.text = currentPlayMusic.name
        nameMusicTextView.text = currentPlayMusic.name
        artistNameTextView.text = currentPlayMusic.artistName
        endTimeTextView.text = currentPlayMusic.duration.let { "${it / 60}: ${it % 60}" }
        musicIconImageView.loadFromUrl(currentPlayMusic.image)
        musicSeekBar.max = currentPlayMusic.duration
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

    private fun checkPermission(): Boolean {
        return activity?.run {
            val result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            result == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    private fun requestPermission() {
        activity?.apply {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val currentMusic = musicPlayer?.currentPlayMusic() ?: return
                val intent = Intent(requireContext(), DownloadMusicService::class.java)
                intent.putExtra(Constant.DOWNLOAD_URL, currentMusic.url)
                intent.putExtra(Constant.NAME_MUSIC, currentMusic.name)
                activity?.startService(intent)
            }
            else -> {
                Snackbar.make(
                    requireView().rootView,
                    "Permission Denied, Please allow to proceed !",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        fun newInstance() = PlayFragment()
    }
}
