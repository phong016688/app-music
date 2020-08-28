package com.example.sunmusic.data.source.remote.configApi

import org.json.JSONArray
import org.json.JSONObject

interface JsonParser {
    fun formJson(jsonObject: JSONObject)
    fun toJson(): JSONObject
}

fun <T : JsonParser> String.toObjectList(clazz: Class<T>, keyData: String = ""): List<T> {
    val result = mutableListOf<T>()
    if (first() == '[' && last() == ']') {
        val jsonArray = JSONArray(this)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val instanceObject = clazz.newInstance().apply { formJson(jsonObject) }
            result.add(instanceObject)
        }
    } else {
        val jsonObject = JSONObject(this)
        if (keyData.isNotEmpty()) {
            return jsonObject.get(keyData).toString().toObjectList(clazz, "")
        }
        val instanceObject = clazz.newInstance().apply { formJson(jsonObject) }
        result.add(instanceObject)
    }
    return result
}
