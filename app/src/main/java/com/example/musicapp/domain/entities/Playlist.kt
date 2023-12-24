package com.example.musicapp.domain.entities

data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistCover: String?,
    val systemTimeMillis: Long
)
