package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.PlaylistsResultListener
import com.example.musicapp.domain.entities.database.PlaylistEntity

class PlaylistsViewModel : ViewModel(), PlaylistsResultListener {

    private val useCase = Creator.playlistsUseCase
    val allPlaylists = MutableLiveData<List<PlaylistEntity>>() // Список плейлистов в БД

    init {
        Creator.setPlaylistUseCaseVM(this)
    }

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

    override fun updateList(list: List<PlaylistEntity>) {
        this.allPlaylists.value = list
    }
}
