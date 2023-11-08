package com.example.musicapp.domain.entities.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insertPlaylist(playlist: PlaylistDBObject)

    @Delete
    fun deletePlaylist(playlist: PlaylistDBObject)

    @Query("SELECT * FROM playlists_table ORDER BY timeMillis desc")
    fun getAll(): LiveData<List<PlaylistDBObject>>
}