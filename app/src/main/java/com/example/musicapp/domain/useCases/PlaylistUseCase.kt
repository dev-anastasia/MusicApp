package com.example.musicapp.domain.useCases

import android.content.Context
import com.example.musicapp.data.database.PlaylistEntity
import com.example.musicapp.presentation.PlaylistsRepo

class PlaylistUseCase(private var repo: PlaylistsRepo) {

    fun addPlaylist(context: Context, playlist: PlaylistEntity) {
        repo.addPlaylist(context, playlist)
    }

    fun deletePlaylist(context: Context, id: Int) {
        repo.deletePlaylist(context, id)
    }

    fun getAllPlaylists(context: Context): List<PlaylistEntity> {
        return repo.getAllPlaylists(context)
    }
}