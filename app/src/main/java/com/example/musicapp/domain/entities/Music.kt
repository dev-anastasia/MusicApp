package com.example.musicapp.domain.entities

data class Music(
    val resultCount: Int,
    val results: MutableList<MusicTrack>
)
