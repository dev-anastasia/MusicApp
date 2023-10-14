package com.example.musicapp.domain.entities

data class MusicPiece(
    val trackId: Long,
    val artistName: String,
    val trackName: String,
    val previewUrl: String,
    val artworkUrl60: String,
)