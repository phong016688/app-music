package com.example.sunmusic.data.model

data class PlayMusic(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val uri: String = "",
    val image: String = "",
    val duration: Int = 0,
    var currentDuration: Int = 0
)
