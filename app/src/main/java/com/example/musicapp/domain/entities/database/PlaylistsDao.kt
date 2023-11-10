package com.example.musicapp.domain.entities.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistsDao {

    // PLAYLISTS
    @Query("SELECT * FROM playlists_table ORDER BY timeMillis desc")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    fun deletePlaylist(playlistId: Int)


    // TRACKS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    fun deleteTrack(trackId: Long)

    @Query("SELECT * FROM tracks_table ORDER BY timeMillis desc")   // Для фрагмента с избранным
    fun getAllTracks(): List<TrackEntity>
}