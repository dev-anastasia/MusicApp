package com.example.musicapp.data.repos

import com.example.musicapp.Creator
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable
import io.reactivex.Single

class PlaylistsRepoImpl : PlaylistsRepo {

    private val mapper = Mapper()

    override fun getPlaylistTracksCount(
        playlistId: Int
    ): Single<List<Long>> {
        return Creator.dao.getTracksIds(playlistId)
    }

    override fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit) {
        val str = Creator.dao.getPlaylistCover(playlistId)
        callback(str.ifEmpty { null })
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
        Creator.dao.deletePlaylist(id)
    }
}