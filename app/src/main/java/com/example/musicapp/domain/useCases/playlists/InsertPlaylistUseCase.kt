package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Completable
import javax.inject.Inject

class InsertPlaylistUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun insertPlaylist(playlist: Playlist): Completable {
        return repo.insertPlaylist(playlist)
    }
}