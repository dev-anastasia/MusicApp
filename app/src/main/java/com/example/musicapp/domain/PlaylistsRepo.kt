package com.example.musicapp.domain

import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable

interface PlaylistsRepo {

    fun getPlaylistTracksCount(playlistId: Int): List<Long>

    fun getPlaylistCover(playlistId: Int): String?

    fun getAllPlaylists(callback: (List<Playlist>) -> Unit)

    fun insertPlaylist(playlist: Playlist): Completable

    fun deletePlaylist(id: Int)
}