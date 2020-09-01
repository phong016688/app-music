package com.example.sunmusic.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.example.sunmusic.R
import com.example.sunmusic.data.executor.AppExecutorImpl
import com.example.sunmusic.data.source.local.CacheImageImpl
import java.net.URL

fun ImageView.loadFromUrl(url: String) {
    val bitmapImageCache = CacheImageImpl.instance.find(url)
    if (bitmapImageCache != null) {
        setImageBitmap(bitmapImageCache)
        return
    }
    val runnable = {
        val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
        Bitmap.createScaledBitmap(bitmap, width * bitmap.width / bitmap.height, height, false)
    }
    val callback: (Bitmap) -> Unit = {
        setImageBitmap(it)
        CacheImageImpl.instance.add(url to it)
    }
    val errorCallback: (Exception) -> Unit = { setImageResource(R.drawable.ic_baseline_error_24) }
    setImageResource(R.drawable.ic_launcher_foreground)
    AppExecutorImpl.instance.runAsyncOnMain(runnable, callback, errorCallback)
}
