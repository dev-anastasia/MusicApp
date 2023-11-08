package com.example.musicapp.presentation.presenters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.repos.PlaylistsRepo
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.entities.room.PlaylistDBObject
import com.example.musicapp.domain.entities.room.PlaylistDatabase

class PlaylistsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: PlaylistsRepo // Позднее - добавить интерфейс!!
    val allPlaylists: LiveData<List<PlaylistDBObject>> // Список объектов типа "плейлист" в БД

    init {
        val playlistDao = PlaylistDatabase.getPlaylistDatabase(app).playlistsDao()
        repo = PlaylistsRepo(playlistDao)
        allPlaylists = repo.allPlaylists
    }

    fun addPlaylist(playlist: PlaylistDBObject) {
        val thread = Thread {
            repo.addPlaylist(playlist)
        }
        thread.start()
        //thread.join()
    }

    fun deletePlaylist(playlist: PlaylistDBObject) {
        val thread = Thread {
            repo.deletePlaylist(playlist)
        }
        thread.start()
        //thread.join()
    }

    private fun update(newList: List<Playlist>) {
        //this.newList.value = newList
    }
}