package com.example.musicapp.domain.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistDBObject(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val cover: String?,
    @ColumnInfo val name: String,
    @ColumnInfo val songCount: Int,
    @ColumnInfo val timeMillis: Long
)