package com.example.musicapp.data.repos

import android.util.Log
import com.example.musicapp.SingletonObjects.dao
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable

class PlaylistsRepoImpl : PlaylistsRepo {

    private val mapper = Mapper()

    override fun getPlaylistTracksCount(
        playlistId: Int
    ): List<Long> {
        return dao!!.getTracksIdsList(playlistId)
    }

    override fun getPlaylistCover(playlistId: Int): String? {
        return dao!!.getPlaylistCover(playlistId).ifEmpty { null }
    }

    override fun getAllPlaylists(callback: (List<Playlist>) -> Unit) {
        val list = dao!!.getAllPlaylists()
        callback(mapper.playlistEntityListToPlaylistList(list))
    }

    override fun insertPlaylist(playlist: Playlist): Completable {
        val playlistTable = mapper.playlistToPlaylistEntity(playlist)
        return dao!!.insertPlaylist(playlistTable)
    }

    override fun deletePlaylist(id: Int) {
        // 1) Удаляем треки плейлиста из БД
        dao!!.getTracksIdsSingle(id)
            .subscribe(
                { list ->
                    for (trackId in list) {
                        dao!!.deleteTrackFromPlaylist(id, trackId)
                    }
                    // Удаляем плелист из БД
                    dao!!.deletePlaylist(id)
                },
                { error ->
                    Log.e("RxJava", "fun deletePlaylist problem: $error")
                }
            )
    }
}
