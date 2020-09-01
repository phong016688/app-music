package com.example.sunmusic.data.source.remote.response

import com.example.sunmusic.data.source.remote.configApi.JsonParser
import org.json.JSONObject

data class ImageResponse(
    var id: String = "",
    var type: String = "",
    var url: String = "",
    var contentId: String = "",
    var width: Int = 0,
    var height: Int = 0
): JsonParser{
    override fun formJson(jsonObject: JSONObject) {
        id = jsonObject.getString("id")
        type = jsonObject.getString("type")
        url = jsonObject.getString("url")
        contentId = jsonObject.getString("contentId")
        width = jsonObject.getInt("width")
        height = jsonObject.getInt("height")
    }

    override fun toJson(): JSONObject = JSONObject()
}
