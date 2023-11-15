package com.example.musicapp.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistsDao {

    // ПЛЕЙЛИСТЫ

    // Получить список всех плейлистов
    @Query("SELECT * FROM playlists_table ORDER BY systemTimeMillis desc")
    fun getAllPlaylists(): List<PlaylistEntity>

    // Добавить плейлист в БД
    @Insert(onConflict = OnConflictStrategy.NONE)
    fun insertPlaylist(playlist: PlaylistEntity)

    // Удалить плейлист из БД
    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)


    // ТРЕКИ

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId")
    fun getTracksIds(playlistId: Int): List<Long>

    // Получить список треков по id
    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId ORDER BY systemTimeMillis desc")
    fun getAllTracksList(trackId: Long): List<TrackEntity>

    // Добавить трек в плейлист
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(ref: PlaylistTrackCrossRef)

    // Удалить трек из плейлиста
    @Query("DELETE FROM cross_ref WHERE trackId = :trackId AND playlistId = :playlistId")
    fun deleteTrack(trackId: Long, playlistId: Int)

    // Добавить трек в плейлист
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

}


    //@Query("SELECT isAddedToMedia FROM favourite_tracks_table WHERE trackId = :id")
    //fun getMediaStatus(id: Long) : Boolean

    // ВСЕ ОСТАЛЬНЫЕ ТРЕКИ
//    @Query("DELETE FROM tracks_table")
//    fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

//    @Transaction
//    @Query("SELECT * FROM playlists_table")
//    fun getPlaylistsWithTracks(): List<PlaylistWithTracks>
