package com.example.musicapp.domain

import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Single

interface PlaylistsRepo {

    fun getPlaylistTracksCount(playlistId: Int): Single<List<Long>>

    fun getTrackCover(trackId: Long): Single<String>

    fun getAllPlaylists(): Single<List<Playlist>>

    fun insertPlaylist(playlist: Playlist)

    fun deletePlaylist(id: Int)
}