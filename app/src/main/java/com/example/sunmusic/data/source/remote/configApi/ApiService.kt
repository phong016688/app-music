package com.example.sunmusic.data.source.remote.configApi

import android.util.Log
import com.example.sunmusic.BuildConfig
import com.example.sunmusic.utils.Constant
import org.json.JSONException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.LazyThreadSafetyMode.SYNCHRONIZED

interface ApiService {
    fun <T : JsonParser> get(query: String, clazz: Class<T>): List<T>
}

class ApiServiceImpl private constructor(
    private val baseUrl: String,
    private val key: String
) : ApiService {
    override fun <T : JsonParser> get(query: String, clazz: Class<T>): List<T> {
        val url = URL("$baseUrl$query?apiKey=$key")
        val urlConnection = url.openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val json = InputStreamReader(urlConnection.inputStream).readText()
            try {
                return json.toObject(clazz)
            } catch (e: JSONException) {
                if (BuildConfig.DEBUG) {
                    Log.e(BuildConfig.APPLICATION_ID, e.message.toString())
                    Log.d("###{${BuildConfig.APPLICATION_ID}", e.message.toString())
                }
            }
        }
        return emptyList()
    }

    companion object {
        val instance by lazy(SYNCHRONIZED) {
            ApiServiceImpl(Constant.BASE_URL, Constant.KEY)
        }
    }
}
