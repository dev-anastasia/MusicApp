package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.PlaylistsRepo

class GetPlaylistsUseCase(private var repo: PlaylistsRepo) {

    fun getAllPlaylists(callback: (List<Playlist>) -> Unit)  {
        repo.getAllPlaylists(callback)
    }
}