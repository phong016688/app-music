package com.example.sunmusic.data.model

data class Album(
    val id: String,
    val type: String,
    val upc: String,
    val shortcut: String,
    val name: String,
    val released: String,
    val label: String,
    val tags: List<String>,
    val trackCount: Int,
    val artistName: String,
    val image: String
)
