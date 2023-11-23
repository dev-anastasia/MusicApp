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
    private val mainHandler = Handler(Looper.getMainLooper())

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

    fun getPlaylistTracksCount(playlistId: Int, context: Context): Int {
        val idsList = mutableListOf<Long>()
        val thread = Thread {
            idsList.addAll(
                Creator.getPlaylistInfoUseCase.getPlaylistTrackCount(
                    playlistId, context
                )
            )
        }
        thread.apply {
            start()
            join()
        }
        return idsList.size
    }

    fun getPlaylistCover(playlistId: Int, context: Context): String? {
        var coverString = ""
        val thread = Thread {
            coverString = Creator.getPlaylistInfoUseCase.getPlaylistCover(
                    playlistId, context
                )
        }
        thread.apply {
            start()
            join()
        }
        return coverString.ifEmpty { null }
    }

    private fun updateList(list: List<PlaylistTable>) {
        this.allPlaylists.value = list
    }
}
