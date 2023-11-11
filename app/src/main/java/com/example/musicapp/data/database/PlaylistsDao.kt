package com.example.musicapp.data.database

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

    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)

//    @Transaction
//    @Query("SELECT * FROM playlists_table")
//    fun getPlaylistsWithTracks(): List<PlaylistWithTracks>
//
//    @Insert(onConflict = OnConflictStrategy.NONE)
//    fun insertPlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

    // ИЗБРАННОЕ
    @Query("SELECT * FROM favourite_tracks_table ORDER BY timeMillis desc")
    fun getFavTracksList(): List<FavTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavTrack(track: FavTrackEntity)

    @Query("DELETE FROM favourite_tracks_table WHERE trackId = :trackId")
    fun deleteFavTrack(trackId: Long)

    //@Query("SELECT isAddedToMedia FROM favourite_tracks_table WHERE trackId = :id")
    //fun getMediaStatus(id: Long) : Boolean

    // ВСЕ ОСТАЛЬНЫЕ ТРЕКИ
//    @Query("DELETE FROM tracks_table")
//    fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

    @Query("SELECT * FROM tracks_table ORDER BY timeMillis desc")   // Для фрагмента с избранным
    fun getAllTracks(): List<TrackEntity>

}