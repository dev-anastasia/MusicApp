package com.example.musicapp.domain.entities.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface PlaylistDao {

    @Upsert     // Insert + Update
    fun insertPlaylist(playlist: Playlist)      // suspend fun?

    @Delete
    fun deletePlaylist(playlist: Playlist)      // suspend fun?
}