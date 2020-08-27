package com.example.sunmusic.data.source.remote.configApi

import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.Error
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface ApiService {
    fun <T : JsonParser> get(router: String, vararg params: String, clazz: Class<T>): List<T>
    fun get(router: String, vararg params: String): String
}

class ApiServiceImpl private constructor(
    private val baseUrl: String,
    private val key: String
) : ApiService {
    override fun <T : JsonParser> get(
        router: String,
        vararg params: String,
        clazz: Class<T>
    ): List<T> {
        val urlString = Constant.createUrlString(baseUrl, key, router, params)
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val json = InputStreamReader(urlConnection.inputStream).readText()
            return json.toObjectList(clazz)
        }
        throw Throwable(Error.CONNECT_HTTP_FAILURE)
    }

    override fun get(router: String, vararg params: String): String {
        val urlString = Constant.createUrlString(baseUrl, key, router, params)
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            return InputStreamReader(urlConnection.inputStream).readText()
        }
        throw Throwable(Error.CONNECT_HTTP_FAILURE)
    }

    companion object {
        val instance by lazy(SYNCHRONIZED) {
            ApiServiceImpl(Constant.BASE_URL, Constant.API_KEY)
        }
    }
}
