package com.example.musicapp.presentation.presenters

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.ui.player.PlayerUIState
import com.example.musicapp.presentation.ui.player.SuccessTrackInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executors

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val uiHandler = Handler(Looper.getMainLooper())
    private var trackId: Long = 0

    val playerUiState = MutableLiveData<PlayerUIState<Int>>()
    val trackInfoLiveData = MutableLiveData<SuccessTrackInfo>()
    val durationStringLiveData = MutableLiveData("0")
    val isLikedLiveData = MutableLiveData(false)
    val isAddedToMediaLiveData = MutableLiveData(false)
    val tracksInThisPlaylistList = MutableLiveData<List<MusicTrack>>(emptyList())

    fun getTrackInfoFromServer(currentId: Long) {

        trackId = currentId

        Creator.getTrackInfoUseCase.getTrackInfo(trackId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val res = response.results[0]
                val trackInfo = SuccessTrackInfo(
                    trackName = MutableLiveData(res.artistName),
                    artistName = MutableLiveData(res.artistName),
                    audioPreview = MutableLiveData(res.previewUrl),
                    artworkUrl100 = MutableLiveData(res.artworkUrl100),
                    artworkUrl60 = MutableLiveData(res.artworkUrl60)
                )
                trackInfoLiveData.postValue(trackInfo)
                updateTrackInfoIfServerRepliedSuccessfully(trackInfo)
            }, { error ->
                Log.e("RxJava", "getTrackInfo fun problem: $error")
                playerUiState.postValue(PlayerUIState.Error)
            }).dispose()
    }

    private fun covertTrackDurationMillisToString() {

        if (durationStringLiveData.value!! != "0:00") {
            val dur = durationStringLiveData.value!!.toLong()
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()

            if (durationInSeconds.length < 2) durationInSeconds =
                "0$durationInSeconds"   // вместо "1:7" -> "1:07"

            durationStringLiveData.postValue("$durationInMinutes:$durationInSeconds")
        }
    }

    override fun updateTrackInfoIfServerRepliedSuccessfully(info: SuccessTrackInfo) {
        val value = trackInfoLiveData.value!!
        if (value.trackName != info.trackName)
            value.trackName = info.trackName
        if (value.artistName != info.artistName)
            value.artistName = info.artistName
        if (value.artworkUrl100 != info.artworkUrl100)
            value.artworkUrl100 = info.artworkUrl100
        if (value.artworkUrl60 != info.artworkUrl60)
            value.artworkUrl60 = info.artworkUrl60
        if (value.audioPreview != info.audioPreview)
            value.audioPreview = info.audioPreview

        if (playerUiState.value != PlayerUIState.Success)
            playerUiState.postValue(PlayerUIState.Success)

        covertTrackDurationMillisToString()
    }

    //Проверка статуса "Нравится"/"Не нравится" (иконка с сердечком)
    fun checkIfFavourite() {
        Creator.getTracksListUseCase.lookForTrackInPlaylist(
            trackId, -1
        ).observeOn(AndroidSchedulers.mainThread()).subscribe({ list ->
            isLikedLiveData.postValue(list.isNotEmpty()) // список непустой = true, в избранном
        }, { error ->
            Log.e("RxJava", "checkIfFavourite fun problem: $error")
            playerUiState.postValue(PlayerUIState.Error)
        })
    }

    fun checkIfAddedToMedia() {
        Creator.getTracksListUseCase.lookForTrackInMedia(trackId)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ list ->
                isAddedToMediaLiveData.postValue(list.isNotEmpty())
            }, { error ->
                Log.e("RxJava", "checkIfAddedToMedia fun problem: $error")
                playerUiState.postValue(PlayerUIState.Error)
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
                    track, Creator.favsPlaylistId
                )
                uiHandler.post {
                    isLikedLiveData.postValue(true)
                }
            } else {
                // Удаляем из избранного:
                Creator.deleteTrackUseCase.deleteTrackFromPlaylist(
                    trackId, Creator.favsPlaylistId
                )
                uiHandler.post {
                    isLikedLiveData.postValue(false)
                }
            }
        }
    }

    // Логика нажатия на иконку "Медиа"
    fun mediaIconClicked(playlistId: Int) {
        // Ищем: есть ли уже трек в выбранном плейлисте? (по аналогии с Яндекс.Музыкой)
        Creator.getTracksListUseCase.lookForTrackInPlaylist(
            trackId, playlistId
        ).subscribe({ list ->
            if (list.isEmpty())         // Если трека нет в этом плейлисте - добавляем
                addToMedia(playlistId)
            else                       // Если трек уже есть в этом плейлисте - удаляем
                deleteFromMedia(playlistId)

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
                uiHandler.post {
                    updateList(it)
                }
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
        uiHandler.post {
            isAddedToMediaLiveData.postValue(true)
        }
    }

    // Запускается из fun mediaIconClicked уже в io-потоке!
    private fun deleteFromMedia(playlistId: Int) {
        Creator.deleteTrackUseCase.deleteTrackFromPlaylist(trackId, playlistId)
        // Обновляем иконку "Медиа" (проверяем, есть ли все еще трек в медиатеке)
        Creator.getTracksListUseCase.lookForTrackInMedia(trackId)
    }

    private fun updateList(list: List<MusicTrack>) {
        tracksInThisPlaylistList.postValue(list)
    }
}