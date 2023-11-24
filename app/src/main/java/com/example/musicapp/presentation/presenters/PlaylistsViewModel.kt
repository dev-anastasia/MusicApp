package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.database.PlaylistTable

class PlaylistsViewModel : ViewModel() {

    val allPlaylists = MutableLiveData<List<PlaylistTable>>() // Список плейлистов в БД
    var addPlaylistFragmentIsOpen = MutableLiveData<Boolean>()
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        addPlaylistFragmentIsOpen.value = false
    }

    fun addPlaylist(context: Context, playlist: PlaylistTable) {
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
        var list = emptyList<PlaylistTable>()
        val thread = Thread {
            list = Creator.getPlaylistsUseCase.getAllPlaylists(context)
        }
        thread.apply {
            start()
            join()
        }
        updateList(list)
    }

    fun getPlaylistTracksCount(playlistId: Int, context: Context, callback: (Int) -> Unit) {
        Creator.getPlaylistInfoUseCase.getPlaylistTrackCount(playlistId, context) {
            callback(it)
        }
    }

    fun getPlaylistCover(playlistId: Int, context: Context, callback: (String?) -> Unit) {
        Creator.getPlaylistInfoUseCase.getPlaylistCover(
            playlistId, context
        ) {
            callback(it.ifEmpty { null })
        }

    }

    private fun updateList(list: List<PlaylistTable>) {
        this.allPlaylists.value = list
    }
}
