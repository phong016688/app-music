package com.example.sunmusic.utils.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import com.example.sunmusic.data.model.PlayMusic

interface BaseMusicPlayer {
    fun addMusics(vararg musics: PlayMusic)
    fun removeMusicsById(vararg musicIds: String)
    fun currentPlayMusic(): PlayMusic?
    fun stopMusic()
    fun clearListMusic()
    fun startMusic()
    fun pauseMusic()
}

class MusicMediaPlayer(private val context: Context) :
    BaseMusicPlayer {
    private var mediaPlayer: MediaPlayer
    private val stateChangeListeners = mutableListOf<MediaListener>()
    private val listMusic = mutableListOf<PlayMusic>()
    private var currentMusic: PlayMusic? = null
    private var state: StateMusic? = null
        set(value) {
            field = value
            if (stateChangeListeners.isNotEmpty() && value != null) {
                stateChangeListeners.forEach { it.onStateChange(value) }
            }
        }

    init {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(getAudioAttributes())
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setOnPreparedListener {
                start()
                state = StateMusic.STARTED
            }
            setOnCompletionListener {
                state = StateMusic.COMPLETED
                reset()
                startMusic()
            }
        }
    }

    override fun addMusics(vararg musics: PlayMusic) {
        val listMusicFilter = musics.filterNot { music -> listMusic.any { it.id == music.id } }
        listMusic.addAll(listMusicFilter)
    }

    override fun removeMusicsById(vararg musicIds: String) {
        listMusic.removeIf { music -> musicIds.any { it == music.id && it != currentMusic?.id } }
        nextCurrentMusic()
    }

    override fun clearListMusic() {
        listMusic.clear()
        nextCurrentMusic()
    }

    override fun startMusic() {
        if (state != StateMusic.STARTED && state != StateMusic.PREPARING) {
            nextCurrentMusic()
            setDateSourceCurrentMusic(currentMusic ?: return)
            mediaPlayer.prepareAsync()
            state = StateMusic.PREPARING
        }
    }

    override fun pauseMusic() {
        if (mediaPlayer.isLooping) {
            mediaPlayer.pause()
            state = StateMusic.PAUSED
        }
    }

    override fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            state = StateMusic.STOP
        }
    }

    override fun currentPlayMusic() = currentMusic

    fun addOnStateChangeListener(listener: MediaListener) {
        stateChangeListeners.add(listener)
    }

    fun removeOnStateChangeListener(listener: MediaListener) {
        stateChangeListeners.remove(listener)
    }

    private fun getAudioAttributes(): AudioAttributes? {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    }

    private fun nextCurrentMusic() {
        val indexOfCM = listMusic.indexOfFirst { it.id == currentMusic?.id }
        currentMusic = when {
            indexOfCM == -1 && listMusic.isNotEmpty() -> listMusic[0]
            indexOfCM in 0..(listMusic.size - 2) -> listMusic[indexOfCM + 1]
            else -> null
        }
    }

    private fun setDateSourceCurrentMusic(music: PlayMusic) {
        try {
            if (music.url.isNotEmpty()) {
                mediaPlayer.setDataSource(music.url)
            } else {
                mediaPlayer.setDataSource(context, Uri.parse(music.uri))
            }
        } catch (e: RuntimeException) {
            nextCurrentMusic()
            setDateSourceCurrentMusic(currentMusic ?: return)
        }
    }

    interface MediaListener {
        fun onStateChange(state: StateMusic)
    }

    companion object {
        private var instance: MusicMediaPlayer? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: MusicMediaPlayer(context).also { instance = it }
        }
    }
}
