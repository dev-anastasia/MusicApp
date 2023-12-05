package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.Playlist

class PlaylistViewModel : ViewModel() {

    val allPlaylists = MutableLiveData<List<Playlist>>(emptyList()) // Список плейлистов в БД
    var addPlaylistFragmentIsOpen = MutableLiveData(false)
    private val uiHandler = Handler(Looper.getMainLooper())

    fun addPlaylist(context: Context, playlist: Playlist) {
        Thread {
            Creator.insertPlaylistUseCase.insertPlaylist(context, playlist)
            getListOfUsersPlaylists(context)
        }.start()
    }

    fun deletePlaylist(context: Context, id: Int) {
        Thread {
            Creator.deletePlaylistUseCase.deletePlaylist(context, id)
            getListOfUsersPlaylists(context)
        }.start()
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
        Thread {
            Creator.getPlaylistInfoUseCase.getPlaylistTrackCount(playlistId, context) {
                callback(it)
            }
        }
    }

    fun getPlaylistCover(playlistId: Int, context: Context, callback: (String?) -> Unit) {
        Thread {
            Creator.getPlaylistInfoUseCase.getPlaylistCover(playlistId, context) {
                uiHandler.post {
                    callback(it.ifEmpty { null })
                }
            }
        }.start()
    }

    private fun updateList(list: List<Playlist>) {
        this.allPlaylists.value = list
    }
}
