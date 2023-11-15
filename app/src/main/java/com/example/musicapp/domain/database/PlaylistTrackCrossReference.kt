package com.example.musicapp.domain.database

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"], tableName = "cross_ref")
data class PlaylistTrackCrossRef(
    val playlistId: Int,
    val trackId: Long
)