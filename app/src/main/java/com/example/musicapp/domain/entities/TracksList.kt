package com.example.musicapp.domain.entities

data class TracksList(
    val resultCount: Int,
    val results: MutableList<TrackInfo>
)
