package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.presentation.PlaylistsRepo

class PlaylistsRepoImpl : PlaylistsRepo {

    override fun getAllPlaylists(context: Context): List<PlaylistTable> {
        return PlaylistDatabase.getDatabase(context).playlistsDao().getAllPlaylists()
    }

    override fun insertPlaylist(context: Context, playlist: PlaylistTable) {
        PlaylistDatabase.getDatabase(context).playlistsDao().insertPlaylist(playlist)
    }

    override fun deletePlaylist(context: Context, id: Int) {
        PlaylistDatabase.getDatabase(context).playlistsDao().deletePlaylist(id)
    }
}