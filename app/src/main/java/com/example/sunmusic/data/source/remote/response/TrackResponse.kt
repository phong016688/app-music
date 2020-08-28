package com.example.sunmusic.data.source.remote.response

import com.example.sunmusic.data.model.Track
import com.example.sunmusic.data.source.remote.configApi.JsonParser
import org.json.JSONObject

data class TrackResponse(
    var id: String = "",
    var type: String = "",
    var index: Int = Int.MIN_VALUE,
    var playbackSeconds: Int = 0,
    var name: String = "",
    var artistId: String = "",
    var artistName: String = "",
    var albumId: String = "",
    var albumName: String = "",
    var previewURL: String = ""
) : JsonParser {
    override fun formJson(jsonObject: JSONObject) {
        id = jsonObject.getString("id")
        type = jsonObject.getString("type")
        index = jsonObject.getInt("index")
        playbackSeconds = jsonObject.getInt("playbackSeconds")
        name = jsonObject.getString("name")
        artistId = jsonObject.getString("artistId")
        artistName = jsonObject.getString("artistName")
        albumId = jsonObject.getString("albumId")
        albumName = jsonObject.getString("albumName")
        previewURL = jsonObject.getString("previewURL")

    }

    override fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("type", type)
        put("index", index)
        put("playbackSeconds", playbackSeconds)
        put("name", name)
        put("artistId", artistId)
        put("artistName", artistName)
        put("albumId", albumId)
        put("albumName", albumName)
        put("previewURL", previewURL)
    }

    fun toTrack() = Track(
        id,
        type,
        index,
        playbackSeconds,
        name,
        artistId,
        artistName,
        albumName,
        albumId,
        previewURL
    )
}
