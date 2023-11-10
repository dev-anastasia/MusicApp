package com.example.musicapp.domain.entities.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossReference(
    @PrimaryKey val crossId: Long,
    val playlistId: Int,
    val trackId: Long
)