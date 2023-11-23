package com.example.musicapp.presentation

import android.content.Context
import com.example.musicapp.domain.database.PlaylistTable

interface PlaylistsRepo {

    fun getAllPlaylists(context: Context) : List<PlaylistTable>

    fun insertPlaylist(context: Context, playlist: PlaylistTable)

    fun deletePlaylist(context: Context, id: Int)
}