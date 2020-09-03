package com.example.sunmusic.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.sunmusic.BuildConfig
import com.example.sunmusic.R
import com.example.sunmusic.screen.main.MainActivity
import com.example.sunmusic.utils.musicplayer.MusicMediaPlayer

class PlayMusicService : Service() {
    private val musicPlayer by lazy { MusicMediaPlayer.getInstance(applicationContext) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_FOREGROUND_SERVICE -> startForegroundService()
            STOP_FOREGROUND_SERVICE -> stopForeground(true)
            START_MUSIC -> musicPlayer.startMusic()
        }
        return START_REDELIVER_INTENT
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE_PENDING_INTENT,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_NOTIFICATION)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(applicationContext.getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(ID_START_FOREGROUND_SERVICE, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID_NOTIFICATION,
                CHANNEL_NAME_NOTIFICATION,
                NotificationManager.IMPORTANCE_DEFAULT
            ).also { getSystemService(NotificationManager::class.java).createNotificationChannel(it) }
        }
    }

    companion object {
        const val CHANNEL_ID_NOTIFICATION = "CHANNEL_ID_IN_${BuildConfig.APPLICATION_ID}"
        const val CHANNEL_NAME_NOTIFICATION = "CHANNEL_NAME_IN_${BuildConfig.APPLICATION_ID}"
        const val REQUEST_CODE_PENDING_INTENT = 0
        const val ID_START_FOREGROUND_SERVICE = 0
        const val START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE"
        const val STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE"
        const val START_MUSIC = "START_MUSIC"
    }
}
