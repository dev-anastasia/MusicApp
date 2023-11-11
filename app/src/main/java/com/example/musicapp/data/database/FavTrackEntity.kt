package com.example.musicapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks_table")
data class FavTrackEntity(
    @PrimaryKey(autoGenerate = false) val trackId: Long,
    @ColumnInfo val artistName: String,
    @ColumnInfo val trackName: String,
    @ColumnInfo val artworkUrl60: String,
    @ColumnInfo val timeMillis: Long,
    // Ниже - var!! Это нормально? Если нет, то как избежать?
    //@ColumnInfo var isAddedToMedia: Boolean
)