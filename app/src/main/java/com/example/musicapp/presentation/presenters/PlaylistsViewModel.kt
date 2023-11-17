package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.database.PlaylistEntity

class PlaylistsViewModel : ViewModel() {

    val allPlaylists = MutableLiveData<List<PlaylistEntity>>() // Список плейлистов в БД

    fun addPlaylist(context: Context, playlist: PlaylistEntity) {
        val thread = Thread {
            Creator.insertPlaylistUseCase.insertPlaylist(context, playlist)
        }
        thread.apply {
            start()
            join()
        }
        getList(context)
    }

    fun deletePlaylist(context: Context, id: Int) {
        val thread = Thread {
            Creator.deletePlaylistUseCase.deletePlaylist(context, id)
        }
        thread.apply {
            start()
            join()
        }
        getList(context)
    }

    fun getList(context: Context) {
        var list = emptyList<PlaylistEntity>()
        val thread = Thread {
            list = Creator.getPlaylistsUseCase.getAllPlaylists(context)
        }
        thread.apply {
            start()
            join()
        }
        updateList(list)
    }

    private fun updateList(list: List<PlaylistEntity>) {
        this.allPlaylists.value = list
    }
}
