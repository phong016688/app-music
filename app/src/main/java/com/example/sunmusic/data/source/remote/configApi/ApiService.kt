package com.example.sunmusic.data.source.remote.configApi

import android.util.Log
import com.example.sunmusic.utils.Constant
import com.example.sunmusic.utils.Error
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface ApiService {
    fun <T : JsonParser> get(
        keyData: String = "",
        clazz: Class<T>,
        router: String,
        vararg params: String
    ): List<T>

    fun get(keyData: String = "", router: String, vararg params: String): String
}

class ApiServiceImpl private constructor(
    private val baseUrl: String,
    private val key: String
) : ApiService {
    override fun <T : JsonParser> get(
        keyData: String,
        clazz: Class<T>,
        router: String,
        vararg params: String
    ): List<T> {
        val urlString = Constant.createUrlString(baseUrl, key, router, params)
        val httpConnection = connectHttpUrl(urlString)
        if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val json = InputStreamReader(httpConnection.inputStream).readText()
            return json.toObjectList(clazz, keyData)
        }
        httpConnection.disconnect()
        throw Throwable("$urlString ${Error.CONNECT_HTTP_FAILURE}")
    }

    override fun get(keyData: String, router: String, vararg params: String): String {
        val urlString = Constant.createUrlString(baseUrl, key, router, params)
        val httpConnection = connectHttpUrl(urlString)
        if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
            return InputStreamReader(httpConnection.inputStream).readText()
        }
        httpConnection.disconnect()
        throw Throwable("$urlString ${Error.CONNECT_HTTP_FAILURE}")
    }

    private fun connectHttpUrl(urlString: String): HttpURLConnection {
        val url = URL(urlString)
        val httpConnection = url.openConnection() as HttpURLConnection
        httpConnection.apply {
            requestMethod = "GET"
            setRequestProperty("Content-length", "0");
            useCaches = false
            allowUserInteraction = false
            connectTimeout = Constant.CONNECT_TIME_OUT
            readTimeout = Constant.READ_TIME_OUT
            connect()
        }
        return httpConnection
    }

    companion object {
        val instance by lazy(SYNCHRONIZED) {
            ApiServiceImpl(Constant.BASE_URL, Constant.API_KEY)
        }
    }
}
