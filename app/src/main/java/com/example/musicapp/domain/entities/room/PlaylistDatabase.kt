package com.example.musicapp.domain.entities.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Playlist::class],
    version = 1
)
abstract class PlaylistDatabase : RoomDatabase() {

    abstract val dao: PlaylistDao
}