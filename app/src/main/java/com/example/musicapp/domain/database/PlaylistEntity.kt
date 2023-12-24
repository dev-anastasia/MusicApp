package com.example.musicapp.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Int,
    @ColumnInfo val playlistName: String,
    @ColumnInfo val playlistCover: String?,
    @ColumnInfo val systemTimeMillis: Long    // Время создания плейлиста
)