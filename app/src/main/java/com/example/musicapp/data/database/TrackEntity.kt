package com.example.musicapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = false) val trackId: Long,
    @ColumnInfo val artistName: String,
    @ColumnInfo val trackName: String,
    @ColumnInfo val artworkUrl60: String,
    @ColumnInfo val timeMillis: Long
)