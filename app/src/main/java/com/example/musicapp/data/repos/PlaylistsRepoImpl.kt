package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.data.Mapper
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.PlaylistsRepo

class PlaylistsRepoImpl : PlaylistsRepo {

    val mapper = Mapper()

    override fun getPlaylistTracksCount(
        playlistId: Int,
        context: Context,
        callback: (Int) -> Unit
    ) {
        val list = PlaylistDatabase.getDatabase(context).dao().getTracksIds(playlistId)
        callback(list.size)
    }

    override fun getPlaylistCover(context: Context, playlistId: Int, callback: (String) -> Unit) {
        val string = PlaylistDatabase.getDatabase(context).dao()
            .getPlaylistCover(playlistId, context)
        callback(string)
    }

    override fun getAllPlaylists(context: Context): List<Playlist> {
        val list = PlaylistDatabase.getDatabase(context).dao().getAllPlaylists()
        return mapper.playlistTableListToPlaylistList(list)
    }

    override fun insertPlaylist(context: Context, playlist: PlaylistTable) {
        PlaylistDatabase.getDatabase(context).dao().insertPlaylist(playlist)
    }

    override fun deletePlaylist(context: Context, id: Int) {
        PlaylistDatabase.getDatabase(context).dao().deletePlaylist(id)
    }
}