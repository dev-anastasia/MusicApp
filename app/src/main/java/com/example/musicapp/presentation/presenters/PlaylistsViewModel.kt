package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.dagger.scopes.MediaScope
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.entities.PlaylistInfo
import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistInfoUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.presentation.ui.media.viewpager.PlaylistsListUiState
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Inject

@MediaScope
class PlaylistsViewModel @Inject constructor(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val insertPlaylistUseCase: InsertPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getPlaylistInfoUseCase: GetPlaylistInfoUseCase
) : ViewModel() {

    val addPlaylistFragmentIsOpen: LiveData<Boolean> // Открыт ли фрагмент с добавлением плейлиста
        get() {
            return _addPlaylistFragmentIsOpen
        }
    private val _addPlaylistFragmentIsOpen = MutableLiveData(false)

    val playlistsListUiState: LiveData<PlaylistsListUiState<Int>>
        get() {
            return _playlistsListUiState
        }
    private val _playlistsListUiState = MutableLiveData<PlaylistsListUiState<Int>>()

    val allPlaylists: LiveData<List<Playlist>> // Список плейлистов в БД
        get() {
            return _allPlaylists
        }
    private val _allPlaylists = MutableLiveData<List<Playlist>>(emptyList())

    fun addPlaylist(playlist: Playlist) {
        insertPlaylistUseCase.insertPlaylist(playlist)
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
            deletePlaylistUseCase.deletePlaylist(id)
            getListOfUsersPlaylists()
        }
    }

    fun getListOfUsersPlaylists() {
        Executors.newSingleThreadExecutor().execute {
            getPlaylistsUseCase.getAllPlaylists {
                if (it.isEmpty()) {
                    _playlistsListUiState.postValue(PlaylistsListUiState.NoResults)
                } else {
                    _playlistsListUiState.postValue(PlaylistsListUiState.Success)
                }
                updateList(it)
            }
        }
    }

    fun getPlaylistInfo(
        playlistId: Int,
        callback: (PlaylistInfo) -> Unit
    ) {
        Executors.newSingleThreadExecutor().execute {
            val playlistCover = getPlaylistInfoUseCase.getPlaylistCover(playlistId)
            val tracksCount = getPlaylistInfoUseCase.getPlaylistTrackCount(playlistId)
            callback(PlaylistInfo(playlistCover, tracksCount.size))
        }
    }

    fun changeAddPlaylistFragmentIsOpen(value: Boolean) {
        _addPlaylistFragmentIsOpen.postValue(value)
    }

    private fun updateList(list: List<Playlist>) {
        _allPlaylists.postValue(list)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "PlaylistsVM onCleared")
    }
}
