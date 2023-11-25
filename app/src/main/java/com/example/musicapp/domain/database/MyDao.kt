package com.example.musicapp.domain.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlaylistsDao {

    // Получить список всех плейлистов
    @Query("SELECT * FROM playlists_table ORDER BY systemTimeMillis desc") // Готово!
    fun getAllPlaylists(): List<PlaylistTable>

    // Добавить плейлист в БД
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Готово!
    fun insertPlaylist(playlist: PlaylistTable)

    // Получить обложку плейлиста (последний трек в коллекции)
    @Transaction
    fun getPlaylistCover(playlistId: Int, context: Context): String {
        val idsList = getTracksIds(playlistId)
        return if (idsList.isEmpty())
            ""
        else
            getTrackCover(idsList[0])
    }

    // Получить обложку трека
    @Query("SELECT artworkUrl60 FROM tracks_table WHERE trackId = :trackId")
    fun getTrackCover(trackId: Long): String

    // Удалить плейлист из БД
    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")  // Готово!
    fun deletePlaylist(playlistId: Int)

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId") // Готово!
    fun getTracksIds(playlistId: Int): List<Long>

    // Получить трек по id
    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId ORDER BY systemTimeMillis asc")
    fun getAllTracksListById(trackId: Long): TrackTable // List?

    // Проверить, есть ли трек в медиатеке (везде кроме Избранного (id = -1))
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId AND playlistId != -1") // Готово!
    fun lookForTrackInPlaylists(trackId: Long): List<Long>

    // Проверить, есть ли трек в БД в целом
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId") // Готово!
    fun lookForTrackInDatabase(trackId: Long): List<Long>

    // Проверить, есть ли трек в медиатеке в КОНКРЕТНОМ плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId AND trackId = :trackId") // Готово!
    fun findTrackInSinglePlaylist(playlistId: Int, trackId: Long): List<Long>

    // Получить список плейлистов, где есть этот трек
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId") // Готово!
    fun getPlaylistsOfThisTrack(trackId: Long): List<Int>

    // Добавить трек в плейлист и в БД
    @Transaction
    fun addTrackToPlaylist(ref: PlaylistTrackCrossRef, track: TrackTable) {
        addPlaylistTrackRef(ref)
        addTrackToDB(track)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylistTrackRef(ref: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDB(track: TrackTable)

    // Удалить трек из плейлиста и из БД (при условии что его нет в других плейлистах)
    @Transaction
    fun deleteTrackFromPlaylist(ref: PlaylistTrackCrossRef, trackId: Long) {
        deletePlaylistTrackCrossRef(ref)
        // Если трека нет в других плейлистах - удаляем его экземпляр из таблицы треков
        val list = lookForTrackInDatabase(trackId)
        if (list.isEmpty())
            deleteTrackFromDB(trackId)
    }

    @Delete
    fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    fun deleteTrackFromDB(trackId: Long)
}