package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import javax.inject.Inject

class GetPlaylistsUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun getAllPlaylists(callback: (List<Playlist>) -> Unit) {
        repo.getAllPlaylists(callback)
    }
}