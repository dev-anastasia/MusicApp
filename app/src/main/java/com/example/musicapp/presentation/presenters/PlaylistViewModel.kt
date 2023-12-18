package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.entities.PlaylistInfo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class PlaylistViewModel() : ViewModel() {

    val addPlaylistFragmentIsOpen: LiveData<Boolean> // Открыт ли фрагмент с добавлением плейлиста
        get() {
            return _addPlaylistFragmentIsOpen
        }
    private val _addPlaylistFragmentIsOpen = MutableLiveData(false)

    val allPlaylists: LiveData<List<Playlist>> // Список плейлистов в БД
        get() {
            return _allPlaylists
        }
    private val _allPlaylists = MutableLiveData<List<Playlist>>(emptyList())

    fun addPlaylist(playlist: Playlist) {
        Creator.insertPlaylistUseCase.insertPlaylist(playlist)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    getListOfUsersPlaylists()
                },
                { error ->
                    Log.e("RxJava", "mediaIconClicked fun problem: $error")
                })
    }

    fun deletePlaylist(id: Int) {
        Executors.newSingleThreadExecutor().execute {
            Creator.deletePlaylistUseCase.deletePlaylist(id)
            getListOfUsersPlaylists()
        }
    }

    fun getListOfUsersPlaylists() {
        Executors.newSingleThreadExecutor().execute {
            Creator.getPlaylistsUseCase.getAllPlaylists {
                updateList(it)
            }
        }
    }

    fun getPlaylistInfo(
        playlistId: Int,
        callback: (PlaylistInfo) -> Unit
    ) {
        Executors.newSingleThreadExecutor().execute {
            val playlistCover = Creator.getPlaylistInfoUseCase.getPlaylistCover(playlistId)
            val tracksCount = Creator.getPlaylistInfoUseCase.getPlaylistTrackCount(playlistId)
            callback(PlaylistInfo(playlistCover, tracksCount.size))
        }
    }

    fun changeAddPlaylistFragmentIsOpen(value: Boolean) {
        _addPlaylistFragmentIsOpen.postValue(value)
    }

    private fun updateList(list: List<Playlist>) {
        _allPlaylists.postValue(list)
    }
}
