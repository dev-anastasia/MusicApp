package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.presentation.PlaylistsRepo

class GetPlaylistsUseCase(private var repo: PlaylistsRepo) {

    fun getAllPlaylists(context: Context): List<PlaylistTable> {
        return repo.getAllPlaylists(context)
    }
}