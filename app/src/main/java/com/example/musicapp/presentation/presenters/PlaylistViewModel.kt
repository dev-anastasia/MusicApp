package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.Playlist

class PlaylistViewModel : ViewModel() {

    val allPlaylists = MutableLiveData<List<Playlist>>() // Список плейлистов в БД
    var addPlaylistFragmentIsOpen = MutableLiveData<Boolean>()
    private val uiHandler = Handler(Looper.getMainLooper())

    init {
        addPlaylistFragmentIsOpen.value = false
    }

    fun addPlaylist(context: Context, playlist: Playlist) {
        Thread {
            Creator.insertPlaylistUseCase.insertPlaylist(context, playlist)
            getListOfUsersPlaylists(context)
        }.start()
    }

    fun deletePlaylist(context: Context, id: Int) {
        val thread = Thread {
            Creator.deletePlaylistUseCase.deletePlaylist(context, id)
        }
        thread.apply {
            start()
            join()
        }
        getListOfUsersPlaylists(context)
    }

    fun getListOfUsersPlaylists(context: Context) {
        Thread {
            Creator.getPlaylistsUseCase.getAllPlaylists(context) {
                uiHandler.post {
                    updateList(it)
                }
            }
        }.start()
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

    private fun updateList(list: List<Playlist>) {
        this.allPlaylists.value = list
    }
}
