package com.example.musicapp.presentation.presenters

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.Playlist
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class PlaylistViewModel : ViewModel() {

    val allPlaylists = MutableLiveData<List<Playlist>>(emptyList()) // Список плейлистов в БД
    var addPlaylistFragmentIsOpen = MutableLiveData(false)
    private val uiHandler = Handler(Looper.getMainLooper())

    fun addPlaylist(playlist: Playlist) {
        Creator.insertPlaylistUseCase.insertPlaylist(playlist).subscribeOn(Schedulers.io())
            .subscribe({
                getListOfUsersPlaylists()
            }, { error ->
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
                uiHandler.post {
                    updateList(it)
                }
            }
        }
    }


    fun getPlaylistTracksCount(playlistId: Int): Single<List<Long>> {
        return Creator.getPlaylistInfoUseCase.getPlaylistTrackCount(playlistId)
    }

    fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit) {
        Creator.getPlaylistInfoUseCase.getPlaylistCover(playlistId) {
            callback(it)
        }
    }

    private fun updateList(list: List<Playlist>) {
        allPlaylists.postValue(list)
    }
}
