package com.example.musicapp.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dagger.Provides
import dagger.assisted.AssistedInject
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@Dao
interface MyDao {

    // Получить список всех плейлистов
    @Query("SELECT * FROM playlists_table ORDER BY systemTimeMillis desc")
    fun getAllPlaylists(): List<PlaylistEntity>

    // Добавить плейлист в БД
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylist(playlist: PlaylistEntity): Completable

    // Получить обложку плейлиста (последний трек в коллекции)
    fun getPlaylistCover(playlistId: Int): String {
        var result = ""
        val tracksIdsList = getTracksIdsList(playlistId)
        if (tracksIdsList.isEmpty().not()) {
            result = getTrackCoverString(tracksIdsList[0])
        }
        return result
    }

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId")
    fun getTracksIdsSingle(playlistId: Int): Single<List<Long>>

    // Получить список id треков в конкретном плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId")
    fun getTracksIdsList(playlistId: Int): List<Long>

    // Получить обложку трека (Single)
    @Query("SELECT artworkUrl60 FROM tracks_table WHERE trackId = :trackId")
    fun getTrackCoverSingle(trackId: Long): Single<String>

    // Получить обложку трека (String)
    @Query("SELECT artworkUrl60 FROM tracks_table WHERE trackId = :trackId")
    fun getTrackCoverString(trackId: Long): String

    // Удалить плейлист из БД
    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    fun deletePlaylist(playlistId: Int)

    // Получить трек по id
    @Query("SELECT * FROM tracks_table WHERE trackId = :trackId ORDER BY systemTimeMillis asc")
    fun getAllTracksListById(trackId: Long): TrackEntity

    // Проверить, есть ли трек в медиатеке - везде КРОМЕ Избранного (id = -1))
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId AND playlistId != -1")
    fun lookForTrackInPlaylists(trackId: Long): Single<List<Long>>

    // Проверить, есть ли трек в медиатеке в КОНКРЕТНОМ плейлисте
    @Query("SELECT trackId FROM cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    fun findTrackInSinglePlaylist(playlistId: Int, trackId: Long): Single<List<Long>>

    // Получить список плейлистов, где есть этот трек
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId")
    fun getPlaylistsOfThisTrack(trackId: Long): List<Int>

    // Добавить трек в плейлист и в БД
    @Transaction
    fun addTrackToPlaylist(track: TrackEntity, playlistId: Int) {
        val crossRef = PlaylistTrackCrossRef(playlistId, track.trackId)
        addPlaylistTrackRef(crossRef)
        addTrackToDB(track)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylistTrackRef(ref: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTrackToDB(track: TrackEntity)

    // Удалить трек из плейлиста и из БД (при условии что его нет в других плейлистах)
    @Transaction
    fun deleteTrackFromPlaylist(playlistId: Int, trackId: Long) {
        val crossRef = PlaylistTrackCrossRef(playlistId, trackId)
        deletePlaylistTrackCrossRef(crossRef)

        // Если трека нет в других плейлистах - удаляем его из таблицы треков
        val list = lookForTrackInDatabase(trackId)
        if (list.isEmpty())
            deleteTrackFromDB(trackId)
    }

    @Delete
    fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRef)

    // Проверить, есть ли трек в БД в целом
    @Query("SELECT trackId FROM cross_ref WHERE trackId = :trackId")
    fun lookForTrackInDatabase(trackId: Long): List<Long>

    @Query("DELETE FROM tracks_table WHERE trackId = :trackId")
    fun deleteTrackFromDB(trackId: Long)
}