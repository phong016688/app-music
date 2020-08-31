package com.example.sunmusic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.sunmusic.R
import com.example.sunmusic.data.executor.AppExecutorImpl
import java.net.URL

fun ImageView.loadFromUrl(url: String) {
    val runnable = { BitmapFactory.decodeStream(URL(url).openStream()) }
    val callback: (Bitmap) -> Unit = { setImageBitmap(it) }
    val errorCallback: (Exception) -> Unit = { setImageResource(R.drawable.ic_baseline_error_24) }
    setImageResource(R.drawable.ic_launcher_foreground)
    AppExecutorImpl.instance.runAsyncOnMain(runnable, callback, errorCallback)
}
