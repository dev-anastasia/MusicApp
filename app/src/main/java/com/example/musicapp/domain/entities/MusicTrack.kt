package com.example.musicapp.domain.entities

data class MusicTrack(
    val trackId: Long,
    val artistName: String,
    val trackName: String,
    val previewUrl: String?,
    val artworkUrl60: String?,
    val artworkUrl100: String?
)
