package com.example.musicapp.presentation

import android.content.Context
import com.example.musicapp.domain.entities.database.PlaylistEntity

interface PlaylistsRepo {

    fun getAllPlaylists(context: Context) : List<PlaylistEntity>

    fun addPlaylist(context: Context, playlist: PlaylistEntity)

    fun deletePlaylist(context: Context, id: Int)
}