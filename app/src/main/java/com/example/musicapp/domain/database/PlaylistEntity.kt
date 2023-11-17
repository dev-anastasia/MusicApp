package com.example.musicapp.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = false) val playlistId: Int,
    @ColumnInfo val cover: String?,
    @ColumnInfo val name: String,
    @ColumnInfo val songCount: Int,
    @ColumnInfo val systemTimeMillis: Long    // Время создания плейлиста
)