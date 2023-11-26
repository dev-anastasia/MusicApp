package com.example.musicapp.domain.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MyDao {

    // Получить список всех плейлистов
    @Query("SELECT * FROM playlists_table ORDER BY systemTimeMillis desc")
    fun getAllPlaylists(): List<PlaylistEntity>

    // Добавить плейлист в БД
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylist(playlist: PlaylistEntity)

    // Получить обложку плейлиста (последний трек в коллекции)
    @Transaction
    fun getPlaylistCover(playlistId: Int, context: Context): String {
        val idsList = getTracksIds(playlistId)
        return if (idsList.isEmpty())
            ""
        else
            getTrackCover(idsList[0])
    }

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId")
    fun getTracksIds(playlistId: Int): List<Long>

    // Получить обложку трека
    @Query("SELECT artworkUrl60 FROM tracks_table WHERE trackId = :trackId")
    fun getTrackCover(trackId: Long): String

    // Удалить плейлист из БД
    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)

    // Получить трек по id
    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId ORDER BY systemTimeMillis asc")
    fun getAllTracksListById(trackId: Long): TrackEntity

    // Проверить, есть ли трек в медиатеке - везде КРОМЕ Избранного (id = -1))
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId AND playlistId != -1")
    fun lookForTrackInPlaylists(trackId: Long): List<Long>

    // Проверить, есть ли трек в БД в целом
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId")
    fun lookForTrackInDatabase(trackId: Long): List<Long>

    // Проверить, есть ли трек в медиатеке в КОНКРЕТНОМ плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    fun findTrackInSinglePlaylist(playlistId: Int, trackId: Long): List<Long>

    // Получить список плейлистов, где есть этот трек
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId")
    fun getPlaylistsOfThisTrack(trackId: Long): List<Int>

    // Добавить трек в плейлист и в БД
    @Transaction
    fun addTrackToPlaylist(ref: PlaylistTrackCrossRef, track: TrackEntity) {
        addPlaylistTrackRef(ref)
        addTrackToDB(track)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylistTrackRef(ref: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDB(track: TrackEntity)

    // Удалить трек из плейлиста и из БД (при условии что его нет в других плейлистах)
    @Transaction
    fun deleteTrackFromPlaylist(ref: PlaylistTrackCrossRef, trackId: Long) {
        deletePlaylistTrackCrossRef(ref)
        // Если трека нет в других плейлистах - удаляем его из таблицы треков
        val list = lookForTrackInDatabase(trackId)
        if (list.isEmpty())
            deleteTrackFromDB(trackId)
    }

    @Delete
    fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    fun deleteTrackFromDB(trackId: Long)
}