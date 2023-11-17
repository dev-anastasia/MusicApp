package com.example.musicapp.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlaylistsDao {

    // Получить список всех плейлистов
    @Query("SELECT * FROM playlists_table ORDER BY systemTimeMillis desc") // Готово!
    fun getAllPlaylists(): List<PlaylistEntity>

    // Добавить плейлист в БД
    @Insert(onConflict = OnConflictStrategy.NONE) // Готово!
    fun insertPlaylist(playlist: PlaylistEntity)

    // Удалить плейлист из БД
    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")  // Готово!
    fun deletePlaylist(playlistId: Int)

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId") // Готово!
    fun getTracksIds(playlistId: Int): List<Long>

    // Добавить трек в плейлист (добавить CrossRef)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylistTrackRef(ref: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore??
    fun addTrackToDB(track: TrackEntity)

    // Получить список треков по id
    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId ORDER BY systemTimeMillis desc")
    fun getAllTracksListById(trackId: Long): List<TrackEntity>

    // Удалить трек из плейлиста
    @Query("DELETE FROM cross_ref WHERE trackId = :trackId AND playlistId = :playlistId")
    fun deleteTrack(trackId: Long, playlistId: Int)

//    // Получить список треков в плейлисте // WHERE playlistId = :playlistId
//    @Transaction
//    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId")
//    fun getPlaylistsWithTracks(trackId: Long): List<PlaylistWithTracks>

    // Проверить, есть ли трек в БД (в каком-либо плейлисте)
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId AND trackId = :trackId") // Готово!
    fun findTrackInDB(playlistId: Int, trackId: Long): List<Long>
}