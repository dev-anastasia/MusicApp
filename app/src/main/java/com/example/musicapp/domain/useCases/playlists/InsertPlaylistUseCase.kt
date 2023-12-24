package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import javax.inject.Inject

class InsertPlaylistUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun insertPlaylist(playlist: Playlist) {
        repo.insertPlaylist(playlist)
    }
}