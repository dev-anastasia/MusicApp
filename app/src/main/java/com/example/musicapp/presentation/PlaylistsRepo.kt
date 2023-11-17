package com.example.musicapp.presentation

import android.content.Context
import com.example.musicapp.domain.database.PlaylistEntity

interface PlaylistsRepo {

    fun getAllPlaylists(context: Context) : List<PlaylistEntity>

    fun insertPlaylist(context: Context, playlist: PlaylistEntity)

    fun deletePlaylist(context: Context, id: Int)
}