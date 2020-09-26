package com.example.sunmusic.service

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.sunmusic.R
import com.example.sunmusic.utils.Constant

class DownloadMusicService : JobIntentService() {
    private lateinit var downloadManager: DownloadManager


    override fun onCreate() {
        super.onCreate()
        downloadManager =
            applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        enqueueWork(
            applicationContext,
            DownloadMusicService::class.java,
            20,
            intent!!
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleWork(intent: Intent) {
        val downloadUrl = intent.getStringExtra(Constant.DOWNLOAD_URL) ?: ""
        val nameMusic = intent.getStringExtra(Constant.NAME_MUSIC) ?: ""
        val title = applicationContext.getString(R.string.dowloading) + " " + nameMusic
        if (downloadUrl.isNotEmpty()) {
            val downloadUri = Uri.parse(downloadUrl)
            val request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setAllowedOverRoaming(false)
            request.setTitle(title)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, nameMusic)
            downloadManager.enqueue(request)
        }
    }
}