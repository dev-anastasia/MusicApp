package com.example.musicapp.domain.entities

data class Playlist(
    val id: Int,    // val id: Int? = null
    val cover: String,
    val name: String,
    val songCount: Int
)