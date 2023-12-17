package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.ui.player.PlayerUIState
import java.util.concurrent.Executors

class PlayerViewModel : ViewModel() {

    private var trackId: Long = 0

    val playerUiState: LiveData<PlayerUIState<Int>>
        get() {
            return _playerUiState
        }
    private val _playerUiState = MutableLiveData<PlayerUIState<Int>>()

    val trackInfoLiveData: LiveData<PlayerUIState.Success.SuccessTrackInfo>
        get() {
            return _trackInfoLiveData
        }
    private val _trackInfoLiveData = MutableLiveData<PlayerUIState.Success.SuccessTrackInfo>()

    val isLikedLiveData: LiveData<Boolean>
        get() {
            return _isLikedLiveData
        }
    private val _isLikedLiveData = MutableLiveData(false)

    val tracksInThisPlaylistList: LiveData<List<MusicTrack>>
        get() {
            return _tracksInThisPlaylistList
        }
    private val _tracksInThisPlaylistList = MutableLiveData<List<MusicTrack>>(emptyList())

    fun getTrackInfoFromServer(currentId: Long) {

        trackId = currentId

        Creator.getTrackInfoUseCase.getTrackInfo(trackId)
            .subscribe(
                { response ->
                    val res = response.results[0]
                    PlayerUIState.Success.updateData(res)
                    if (playerUiState.value != PlayerUIState.Success) {
                        _playerUiState.postValue(PlayerUIState.Success)
                    }
                },
                { error ->
                    Log.e("RxJava", "getTrackInfo fun problem: $error")
                    _playerUiState.postValue(PlayerUIState.Error)
                }
            )
    }

    //Проверка статуса "Нравится"/"Не нравится" (иконка с сердечком)
    fun checkIfFavourite() {
        Creator.getTracksListUseCase.lookForTrackInPlaylist(
            trackId, Creator.dao.favsPlaylistId()
        )
            .subscribe(
                { list ->
                    _isLikedLiveData.postValue(list.isNotEmpty()) // список непустой = true, в избранном
                },
                { error ->
                    Log.e("RxJava", "checkIfFavourite fun problem: $error")
                    _playerUiState.postValue(PlayerUIState.Error)
                })
    }

    // Логика нажатия на кнопку "Нравится"
    fun likeClicked() {
        Executors.newSingleThreadExecutor().execute {
            if (isLikedLiveData.value == false) {
                // Добавляем в избранное:
                val ld = trackInfoLiveData.value!!
                val track = MusicTrack(
                    trackId = trackId,
                    trackName = ld.trackName.value!!,
                    artistName = ld.artistName.value!!,
                    previewUrl = ld.audioPreview.value,
                    artworkUrl100 = ld.artworkUrl100.value,
                    artworkUrl60 = ld.artworkUrl60.value
                )
                Creator.insertTrackUseCase.addTrackToPlaylist(
                    track, Creator.dao.favsPlaylistId()
                )
                _isLikedLiveData.postValue(true)
            } else {
                // Удаляем из избранного:
                Creator.deleteTrackUseCase.deleteTrackFromPlaylist(
                    trackId, Creator.dao.favsPlaylistId()
                )
                _isLikedLiveData.postValue(false)
            }
        }
    }

    // Логика нажатия на иконку "Медиа"
    fun mediaIconClicked(playlistId: Int) {
        // Ищем: есть ли уже трек в выбранном плейлисте? (по аналогии с Яндекс.Музыкой)
        Creator.getTracksListUseCase.lookForTrackInPlaylist(
            trackId, playlistId
        ).subscribe({ list ->
            if (list.isEmpty()) {         // Если трека нет в этом плейлисте - добавляем
                addToMedia(playlistId)
            } else {                      // Если трек уже есть в этом плейлисте - удаляем
                deleteFromMedia(playlistId)
            }
        }, { error ->
            Log.e("RxJava", "mediaIconClicked fun problem: $error")
        })
    }

    fun getListOfUsersPlaylists(callback: (List<Playlist>) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            Creator.getPlaylistsUseCase.getAllPlaylists(callback)
        }
    }

    fun getTracksList(playlistId: Int) {
        Executors.newSingleThreadExecutor().execute {
            Creator.getTracksListUseCase.getPlaylistTracksList(playlistId) {
                updateList(it)
            }
        }
    }

    // Запускается из fun mediaIconClicked уже в io-потоке!
    private fun addToMedia(playlistId: Int) {
        val ld = trackInfoLiveData.value!!
        val track = MusicTrack(
            trackId = trackId,
            trackName = ld.trackName.value!!,
            artistName = ld.artistName.value!!,
            previewUrl = ld.audioPreview.value,
            artworkUrl100 = ld.artworkUrl100.value,
            artworkUrl60 = ld.artworkUrl60.value
        )
        Creator.insertTrackUseCase.addTrackToPlaylist(track, playlistId)
    }

    // Запускается из fun mediaIconClicked уже в io-потоке!
    private fun deleteFromMedia(playlistId: Int) {
        Creator.deleteTrackUseCase.deleteTrackFromPlaylist(trackId, playlistId)
        // Обновляем иконку "Медиа" (проверяем, есть ли все еще трек в медиатеке)
        Creator.getTracksListUseCase.lookForTrackInMedia(trackId)
    }

    private fun updateList(list: List<MusicTrack>) {
        _tracksInThisPlaylistList.postValue(list)
    }
}