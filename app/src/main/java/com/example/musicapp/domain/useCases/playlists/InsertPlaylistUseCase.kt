package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.PlaylistsRepo

class InsertPlaylistUseCase(private var repo: PlaylistsRepo) {

    fun insertPlaylist(context: Context, playlist: Playlist) {
        repo.insertPlaylist(context, playlist)
    }
}