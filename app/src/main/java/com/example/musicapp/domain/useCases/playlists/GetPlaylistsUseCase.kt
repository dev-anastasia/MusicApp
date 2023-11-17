package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.database.PlaylistEntity
import com.example.musicapp.presentation.PlaylistsRepo

class GetPlaylistsUseCase(private var repo: PlaylistsRepo) {

    fun getAllPlaylists(context: Context): List<PlaylistEntity> {
        return repo.getAllPlaylists(context)
    }
}