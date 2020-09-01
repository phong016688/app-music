package com.example.sunmusic.data.source.local

import android.graphics.Bitmap

interface CacheImage {
    fun add(element: Pair<String, Bitmap>)
    fun remove(url: String)
    fun find(url: String): Bitmap?
    fun clear()
}

class CacheImageImpl private constructor() : CacheImage {
    private val images = hashMapOf<String, Bitmap>()

    override fun add(element: Pair<String, Bitmap>) {
        images[element.first] = element.second
    }

    override fun remove(url: String) {
        images.remove(url)
    }

    override fun find(url: String): Bitmap? = images[url]

    override fun clear() {
        images.clear()
    }

    companion object {
        val instance by lazy { CacheImageImpl() }
    }
}
