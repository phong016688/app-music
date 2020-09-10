package com.example.sunmusic.utils.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import com.example.sunmusic.data.model.PlayMusic
import com.example.sunmusic.utils.Constant

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
    private var currentPosition: Int = Constant.NO_EXIT
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
            }
        }
    }

    override fun addMusics(vararg musics: PlayMusic) {
        val listMusicFilter = musics.filterNot { music -> listMusic.any { it.id == music.id } }
        listMusic.addAll(listMusicFilter)
    }

    override fun removeMusicsById(vararg musicIds: String) {
        musicIds.forEach { musicId ->
            val positionRemove = listMusic.indexOfFirst { it.id == musicId }
            if (positionRemove != Constant.NO_EXIT)
                listMusic.removeAt(positionRemove)
        }
    }

    override fun clearListMusic() {
        listMusic.clear()
        nextMusic()
    }

    override fun startMusic() {
        val stateCanStart = state != StateMusic.STARTED && state != StateMusic.PREPARING
        if (stateCanStart && currentPosition in listMusic.indices) {
            setDateSourceCurrentMusic(listMusic[currentPosition])
            mediaPlayer.prepareAsync()
            state = StateMusic.PREPARING
        }
    }

    override fun pauseMusic() {
        if (mediaPlayer.isPlaying && state == StateMusic.STARTED) {
            mediaPlayer.pause()
            state = StateMusic.PAUSED
        }
    }

    override fun stopMusic() {
        val stateCanStop = state == StateMusic.STARTED || state == StateMusic.PAUSED
        if (mediaPlayer.isPlaying && stateCanStop) {
            mediaPlayer.stop()
            state = StateMusic.STOP
        }
    }

    override fun currentPlayMusic() = listMusic.elementAtOrElse(currentPosition) { null }

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

    private fun nextMusic() {
        currentPosition++
    }

    private fun setDateSourceCurrentMusic(music: PlayMusic) {
        try {
            if (music.uri.isNotEmpty()) {
                mediaPlayer.setDataSource(context, Uri.parse(music.url))
            } else {
                mediaPlayer.setDataSource(music.url)
            }
        } catch (e: RuntimeException) {
            state = StateMusic.ERROR
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
