package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.data.database.PlaylistEntity
import com.example.musicapp.data.database.PlaylistDatabase
import com.example.musicapp.presentation.PlaylistsRepo

class PlaylistsRepoImpl : PlaylistsRepo {

    override fun getAllPlaylists(context: Context): List<PlaylistEntity> {
        return PlaylistDatabase.getDatabase(context).playlistsDao().getAllPlaylists()
    }

    override fun addPlaylist(context: Context, playlist: PlaylistEntity) {
        PlaylistDatabase.getDatabase(context).playlistsDao().insertPlaylist(playlist)
    }

    override fun deletePlaylist(context: Context, id: Int) {
        PlaylistDatabase.getDatabase(context).playlistsDao().deletePlaylist(id)
    }
}