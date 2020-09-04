package com.example.sunmusic.data.model

data class Track(
  val id: String,
  val type: String,
  val index: Number,
  val playbackSeconds: Int,
  val name: String,
  val artistId: String,
  val artistName: String,
  val albumName: String,
  val albumId: String,
  val previewURL: String,
  val image: String
)
