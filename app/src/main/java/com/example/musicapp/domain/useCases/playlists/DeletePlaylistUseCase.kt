package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo

class DeletePlaylistUseCase(private var repo: PlaylistsRepo) {

    fun deletePlaylist(id: Int) {
        repo.deletePlaylist(id)
    }
}