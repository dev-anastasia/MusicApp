package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun deletePlaylist(id: Int) {
        repo.deletePlaylist(id)
    }
}