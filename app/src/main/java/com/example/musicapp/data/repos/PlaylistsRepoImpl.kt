package com.example.musicapp.data.repos

import com.example.musicapp.Creator
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable
import io.reactivex.Single

class PlaylistsRepoImpl : PlaylistsRepo {

    private val mapper = Mapper()
    private val dao = Creator.dao!!

    override fun getPlaylistTracksCount(
        playlistId: Int
    ): Single<List<Long>> {
        return dao.getTracksIds(playlistId)
    }

    override fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit) {
        val str = dao.getPlaylistCover(playlistId)
        callback(str.ifEmpty { null })
    }

    override fun getAllPlaylists(callback: (List<Playlist>) -> Unit) {
        val list = dao.getAllPlaylists()
        callback(mapper.playlistEntityListToPlaylistList(list))
    }

    override fun insertPlaylist(playlist: Playlist): Completable {
        val playlistTable = mapper.playlistToPlaylistEntity(playlist)
        return dao.insertPlaylist(playlistTable)
    }

    override fun deletePlaylist(id: Int) {
        dao.deletePlaylist(id)
    }
}