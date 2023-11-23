package com.example.musicapp.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistTable(
    @PrimaryKey(autoGenerate = true) val playlistId: Int,
    @ColumnInfo val playlistCover: String?,
    @ColumnInfo val playlistName: String,
    @ColumnInfo val systemTimeMillis: Long    // Время создания плейлиста
)