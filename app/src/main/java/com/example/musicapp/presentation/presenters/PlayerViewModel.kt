package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.SingletonObjects.dao
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.domain.entities.TrackInfo
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.ui.player.PlayerUIState
import com.example.musicapp.presentation.ui.player.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executors
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val getTracksListUseCase: GetTracksListUseCase,
    private val insertTrackUseCase: InsertTrackUseCase,
    private val deleteTrackUseCase: DeleteTrackUseCase,
    private val getTrackInfoUseCase: GetTrackInfoUseCase
) : ViewModel() {

    private var trackId: Long = 0

    val playerUiState: LiveData<PlayerUIState<Int>>
        get() {
            return _playerUiState
        }
    private val _playerUiState = MutableLiveData<PlayerUIState<Int>>()

    val viewState: LiveData<ViewState>
        get() {
            return _viewState
        }
    private val _viewState = MutableLiveData(ViewState())

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

    private fun currentViewState(): ViewState = viewState.value!!

    fun getTrackInfoFromServer(currentId: Long) {

        trackId = currentId

        getTrackInfoUseCase.getTrackInfo(trackId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    updateViewStateInfo(response.results[0])

                    if (playerUiState.value != PlayerUIState.Success) {     // УДАЛИТЬ?
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
        getTracksListUseCase.lookForTrackInPlaylist(
            trackId, dao!!.favsPlaylistId()
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
                val viewStateInfo = viewState.value!!
                val track = MusicTrack(
                    trackId = trackId,
                    trackName = viewStateInfo.trackName,
                    artistName = viewStateInfo.artistName,
                    previewUrl = viewStateInfo.audioPreview,
                    artworkUrl100 = viewStateInfo.artworkUrl100,
                    artworkUrl60 = viewStateInfo.artworkUrl60
                )
                insertTrackUseCase.addTrackToPlaylist(
                    track, dao!!.favsPlaylistId()
                )
                _isLikedLiveData.postValue(true)
            } else {
                // Удаляем из избранного:
                deleteTrackUseCase.deleteTrackFromPlaylist(
                    trackId, dao!!.favsPlaylistId()
                )
                _isLikedLiveData.postValue(false)
            }
        }
    }

    // Логика нажатия на иконку "Медиа"
    fun mediaIconClicked(playlistId: Int) {
        // Ищем: есть ли уже трек в выбранном плейлисте? (по аналогии с Яндекс.Музыкой)
        getTracksListUseCase.lookForTrackInPlaylist(
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
            getPlaylistsUseCase.getAllPlaylists(callback)
        }
    }

    fun getTracksList(playlistId: Int) {
        Executors.newSingleThreadExecutor().execute {
            getTracksListUseCase.getPlaylistTracksList(playlistId) {
                updateList(it)
            }
        }
    }

    // Запускается из fun mediaIconClicked уже в io-потоке!
    private fun addToMedia(playlistId: Int) {
        val ld = viewState.value!!
        val track = MusicTrack(
            trackId = trackId,
            trackName = ld.trackName,
            artistName = ld.artistName,
            previewUrl = ld.audioPreview,
            artworkUrl100 = ld.artworkUrl100,
            artworkUrl60 = ld.artworkUrl60
        )
        insertTrackUseCase.addTrackToPlaylist(track, playlistId)
    }

    // Запускается из fun mediaIconClicked уже в io-потоке!
    private fun deleteFromMedia(playlistId: Int) {
        deleteTrackUseCase.deleteTrackFromPlaylist(trackId, playlistId)
        getTracksListUseCase.lookForTrackInMedia(trackId)
    }

    private fun updateList(list: List<MusicTrack>) {
        _tracksInThisPlaylistList.postValue(list)
    }

    private fun updateViewStateInfo(res: TrackInfo) {
        if (_viewState.value?.trackName != res.trackName) {
            _viewState.value = currentViewState().copy(trackName = res.trackName)
        }
        if (_viewState.value?.artistName != res.artistName) {
            _viewState.value = currentViewState().copy(artistName = res.artistName)
        }
        if (_viewState.value?.audioPreview != res.previewUrl) {
            _viewState.value = currentViewState().copy(audioPreview = res.previewUrl)
        }
        if (_viewState.value?.artworkUrl60 != res.artworkUrl60) {
            _viewState.value = currentViewState().copy(artworkUrl60 = res.artworkUrl60)
        }
        if (_viewState.value?.artworkUrl100 != res.artworkUrl100) {
            _viewState.value = currentViewState().copy(artworkUrl100 = res.artworkUrl100)
        }
    }

    private companion object {
        const val DURATION_DEFAULT = "0:00"
    }
}