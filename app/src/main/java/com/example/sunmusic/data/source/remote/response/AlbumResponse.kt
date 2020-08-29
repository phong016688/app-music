package com.example.sunmusic.data.source.remote.response

import com.example.sunmusic.data.model.Album
import com.example.sunmusic.data.source.remote.configApi.JsonParser
import com.example.sunmusic.utils.toList
import org.json.JSONObject

data class AlbumResponse(
    var id: String = "",
    var type: String = "",
    var upc: String = "",
    var shortcut: String = "",
    var name: String = "",
    var released: String = "",
    var label: String = "",
    var tags: List<String> = emptyList(),
    var trackCount: Int = 0,
    var artistName: String = ""
) : JsonParser {
    override fun formJson(jsonObject: JSONObject) {
        id = jsonObject.getString("id")
        type = jsonObject.getString("type")
        upc = jsonObject.getString("upc")
        shortcut = jsonObject.getString("shortcut")
        name = jsonObject.getString("name")
        released = jsonObject.getString("released")
        label = jsonObject.getString("label")
        tags = jsonObject.getJSONArray("tags").toList()
        trackCount = jsonObject.getInt("trackCount")
        artistName = jsonObject.getString("artistName")
    }

    override fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("type", type)
        put("upc", upc)
        put("shortcut", shortcut)
        put("name", name)
        put("released", released)
        put("label", label)
        put("tags", tags)
        put("trackCount", trackCount)
        put("artistName", artistName)
    }

    fun toAlbum() = Album(
        id,
        type,
        upc,
        shortcut,
        name,
        released,
        label,
        tags,
        trackCount,
        artistName
    )
}

