package com.example.musicapp.domain

import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable
import io.reactivex.Single

interface PlaylistsRepo {

    fun getPlaylistTracksCount(playlistId: Int): Single<List<Long>>

    fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit)

    fun getAllPlaylists(callback: (List<Playlist>) -> Unit)

    fun insertPlaylist(playlist: Playlist): Completable

    fun deletePlaylist(id: Int)
}