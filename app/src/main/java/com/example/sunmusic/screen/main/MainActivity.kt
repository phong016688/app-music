package com.example.sunmusic.screen.main

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sunmusic.R
import com.example.sunmusic.service.PlayMusicService
import com.example.sunmusic.utils.replaceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var musicService: PlayMusicService? = null
    private val musicConnectionService by lazy { getConnectionService() }

    private fun getConnectionService(): ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(componentName: ComponentName?) {
            musicService = null
        }

        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            musicService = (binder as? PlayMusicService.LocalBinder)?.getService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(MainFragment.newInstance(), R.id.container)
        bindMusicService()
    }

    private fun bindMusicService() {
        Intent(this, PlayMusicService::class.java).also {
            bindService(it, musicConnectionService, Service.BIND_AUTO_CREATE)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, PlayMusicService::class.java).also {
            it.action = "STOP_FOREGROUND_REMOVE"
            startService(it)
        }
    }

    override fun onStop() {
        super.onStop()
        if (musicService?.musicPlayer?.currentPlayMusic() == null) return
        Intent(this, PlayMusicService::class.java).also {
            startService(it)
        }
    }

    override fun onDestroy() {
        unbindService(musicConnectionService)
        musicService = null
        super.onDestroy()
    }

    fun getMusicService() = musicService
}
