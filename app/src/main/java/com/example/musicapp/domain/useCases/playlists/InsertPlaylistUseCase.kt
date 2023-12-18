package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.PlaylistsRepo
import io.reactivex.Completable

class InsertPlaylistUseCase(val repo: PlaylistsRepo) {

    fun insertPlaylist(playlist: Playlist): Completable {
        return repo.insertPlaylist(playlist)
    }
}