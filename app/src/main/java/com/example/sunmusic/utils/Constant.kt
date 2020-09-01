package com.example.sunmusic.utils

import com.example.sunmusic.BuildConfig

object Constant {
    const val BASE_URL = BuildConfig.BASE_URL
    const val API_KEY = BuildConfig.API_KEY
    const val CONNECT_TIME_OUT = 10000
    const val READ_TIME_OUT = 10000
    const val DEFAULT_TOP_ALBUM_COUNT = 3
    const val DEFAULT_TOP_TRACK_COUNT = 10

    fun createUrlString(
        baseUrl: String,
        key: String,
        router: String,
        params: Array<out String>
    ): String {
        return "$baseUrl$router?apikey=$key&${params.joinToString(prefix = "&", separator = "&")}"
    }
}
