package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.presentation.PlaylistsRepo

class DeletePlaylistUseCase(private var repo: PlaylistsRepo) {

    fun deletePlaylist(context: Context, id: Int) {
        repo.deletePlaylist(context, id)
    }
}