package com.example.sunmusic.utils.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import com.example.sunmusic.data.model.PlayMusic
import com.example.sunmusic.utils.Constant

interface BaseMusicPlayer {
    fun addMusics(vararg musics: PlayMusic)
    fun removeMusicsById(vararg musicIds: String)
    fun currentPlayMusic(): PlayMusic?
    fun moveToMusic(musicId: String)
    fun state(): StateMusic?
    fun durationStarted(): Int
    fun nextMusic()
    fun backMusic()
    fun stopMusic()
    fun clearListMusic()
    fun startMusic()
    fun pauseMusic()
    fun playMusic()
}

class MusicMediaPlayer(private val context: Context) :
    BaseMusicPlayer {
    val listMusic = mutableListOf<PlayMusic>()
    private var mediaPlayer: MediaPlayer
    private val stateChangeListeners = mutableListOf<MediaListener>()
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
                nextMusic()
                startMusic()
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
            mediaPlayer.reset()
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

    override fun playMusic() {
        if (mediaPlayer.isPlaying && state == StateMusic.PAUSED) {
            mediaPlayer.start()
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
        listener.onStateChange(state ?: return)
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

    override fun nextMusic() {
        currentPosition++
        if (currentPosition !in listMusic.indices) {
            currentPosition = Constant.NO_EXIT
        }
    }

    override fun backMusic() {
        currentPosition--
        if (currentPosition !in listMusic.indices) {
            currentPosition = Constant.NO_EXIT
        }
    }

    override fun moveToMusic(musicId: String) {
        val position = listMusic.indexOfFirst { it.id == musicId }
        if (position != Constant.NO_EXIT) {
            currentPosition = position
        }
    }

    override fun state() = state

    override fun durationStarted() = mediaPlayer.currentPosition

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
