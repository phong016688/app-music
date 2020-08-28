package com.example.sunmusic.utils

import org.json.JSONArray

inline fun <reified T> JSONArray.toList(): List<T> {
    val result = mutableListOf<T>()
    when (T::class.java) {
        String::class.java -> {
            for (index in 0 until length()) {
                result.add(getString(index) as T)
            }
        }
        Int::class.java -> {
            for (index in 0 until length()) {
                result.add(getInt(index) as T)
            }
        }
        Boolean::class.java -> {
            for (index in 0 until length()) {
                result.add(getBoolean(index) as T)
            }
        }
        Double::class.java -> {
            for (index in 0 until length()) {
                result.add(getDouble(index) as T)
            }
        }
        Long::class.java -> {
            for (index in 0 until length()) {
                result.add(getLong(index) as T)
            }
        }
    }
    return result
}
