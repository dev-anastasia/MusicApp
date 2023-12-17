package com.example.musicapp.data.repos

import android.util.Log
import com.example.musicapp.Creator
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable

class PlaylistsRepoImpl : PlaylistsRepo {

    private val mapper = Mapper()

    override fun getPlaylistTracksCount(
        playlistId: Int
    ): List<Long> {
        return Creator.dao.getTracksIdsList(playlistId)
    }

    override fun getPlaylistCover(playlistId: Int): String? {
        return Creator.dao.getPlaylistCover(playlistId).ifEmpty { null }
    }

    override fun getAllPlaylists(callback: (List<Playlist>) -> Unit) {
        val list = Creator.dao.getAllPlaylists()
        callback(mapper.playlistEntityListToPlaylistList(list))
    }

    override fun insertPlaylist(playlist: Playlist): Completable {
        val playlistTable = mapper.playlistToPlaylistEntity(playlist)
        return Creator.dao.insertPlaylist(playlistTable)
    }

    override fun deletePlaylist(id: Int) {
        // 1) Удаляем треки плейлиста из БД
        Creator.dao.getTracksIdsSingle(id)
            .subscribe(
                { list ->
                    for (trackId in list) {
                        Creator.dao.deleteTrackFromPlaylist(id, trackId)
                    }
                    // Удаляем плелист из БД
                    Creator.dao.deletePlaylist(id)
                },
                { error ->
                    Log.e("RxJava", "fun deletePlaylist problem: $error")
                }
            )
    }
}
