package com.example.musicapp.data.repos

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.entities.room.PlaylistsDao
import com.example.musicapp.domain.entities.room.PlaylistDBObject

class PlaylistsRepo(private val dao: PlaylistsDao) {

    val allPlaylists: LiveData<List<PlaylistDBObject>> = dao.getAll()

    fun addPlaylist(playlist: PlaylistDBObject) {
        dao.insertPlaylist(playlist)
    }

    fun deletePlaylist(playlist: PlaylistDBObject) {
        dao.deletePlaylist(playlist)
    }
}