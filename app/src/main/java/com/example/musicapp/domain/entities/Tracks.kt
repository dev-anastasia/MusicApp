package com.example.musicapp.domain.entities

data class Tracks(
    val resultCount: Int,
    val results: MutableList<TrackInfo>
)
