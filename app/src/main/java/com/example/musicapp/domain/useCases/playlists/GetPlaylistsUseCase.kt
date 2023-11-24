package com.example.musicapp.domain.useCases.playlists

import android.content.Context
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.PlaylistsRepo

class GetPlaylistsUseCase(private var repo: PlaylistsRepo) {

    fun getAllPlaylists(context: Context, callback: (List<Playlist>) -> Unit)  {
        val list = repo.getAllPlaylists(context)
        callback(list)
    }
}