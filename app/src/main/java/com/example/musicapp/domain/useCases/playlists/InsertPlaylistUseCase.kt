package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.database.PlaylistEntity
import com.example.musicapp.presentation.PlaylistsRepo

class InsertPlaylistUseCase(private var repo: PlaylistsRepo) {

    fun insertPlaylist(context: Context, playlist: PlaylistEntity) {
        repo.insertPlaylist(context, playlist)
    }
}