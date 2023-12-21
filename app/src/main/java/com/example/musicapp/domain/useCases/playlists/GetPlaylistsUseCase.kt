package com.example.musicapp.domain.useCases.playlists

import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Single
import javax.inject.Inject

class GetPlaylistsUseCase @Inject constructor(val repo: PlaylistsRepo) {

    fun getAllPlaylists(): Single<List<Playlist>> {
        return repo.getAllPlaylists()
    }
}