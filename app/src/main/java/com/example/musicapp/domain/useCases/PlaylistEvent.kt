package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.entities.room.Playlist

sealed interface PlaylistEvent {

    object savePlaylist: PlaylistEvent
    data class savePlaylistName(val name: String): PlaylistEvent
    data class deletePlaylist(val playlist: Playlist): PlaylistEvent
}