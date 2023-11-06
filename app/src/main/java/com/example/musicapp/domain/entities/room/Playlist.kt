package com.example.musicapp.domain.entities.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = false)
    val id: Int,    // val id: Int? = null
    val cover: String,
    val name: String,
    val songCount: Int
)