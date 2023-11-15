package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.database.PlaylistEntity

class PlaylistsViewModel : ViewModel() {

    private val useCase = Creator.playlistsUseCase
    val allPlaylists = MutableLiveData<List<PlaylistEntity>>() // Список плейлистов в БД

    fun addPlaylist(context: Context, playlist: PlaylistEntity) {
        val thread = Thread {
            useCase.addPlaylist(context, playlist)
        }
        thread.apply {
            start()
            join()
        }
        getList(context)
    }

    fun deletePlaylist(context: Context, id: Int) {
        val thread = Thread {
            useCase.deletePlaylist(context, id)
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
            list = useCase.getAllPlaylists(context)
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