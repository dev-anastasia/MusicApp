package com.example.musicapp.domain.entities

data class TrackInfo(
    val artistName: String,
    val trackName: String,
    val previewUrl: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val artworkUrl60: String
)
