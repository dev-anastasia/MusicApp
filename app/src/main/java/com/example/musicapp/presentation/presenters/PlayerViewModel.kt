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
import com.example.musicapp.presentation.ui.player.TrackInfoSpecs
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executors

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val uiHandler = Handler(Looper.getMainLooper())
    val playerUiState = MutableLiveData<PlayerUIState<Any>>()

    //    val trackNameLiveData = MutableLiveData("")
//    val artistNameLiveData = MutableLiveData("")
//    val durationLiveData = MutableLiveData("0")
//    val audioPreviewLiveData = MutableLiveData("")
//    val cover100LiveData = MutableLiveData<String>(null)  // Больший размер (для плеера)
//    private val cover60LiveData = MutableLiveData<String>(null)   // Меньший размер (для БД)
    val isLikedLiveData = MutableLiveData(false)
    val isAddedToMediaLiveData = MutableLiveData(false)
    val tracksInThisPlaylistList = MutableLiveData<List<MusicTrack>>(emptyList())

    private var trackId: Long = 0

    fun getTrackInfoFromServer(currentId: Long) {

        trackId = currentId

        Creator.getTrackInfoUseCase.getTrackInfo(trackId)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                val res = response.results[0]
                updateSuccessUiState(
                    TrackInfoSpecs(
                        res.artistName,
                        res.artistName,
                        res.previewUrl,
                        res.trackTimeMillis.toString(),
                        res.artworkUrl100,
                        res.artworkUrl60
                    )
                )
            }, { error ->
                Log.e("RxJava", "getTrackInfo fun problem: $error")
                playerUiState.postValue(PlayerUIState.Error)
            })
    }

    private fun covertTrackDurationMillisToString() {

        if (durationLiveData.value!! != "0:00") {
            val dur = durationLiveData.value!!.toLong()
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()

            if (durationInSeconds.length < 2) durationInSeconds =
                "0$durationInSeconds"   // вместо "1:7" -> "1:07"

            durationLiveData.postValue("$durationInMinutes:$durationInSeconds")
        }
    }

    override fun updateSuccessUiState(info: TrackInfoSpecs) {
        playerUiState.postValue(PlayerUIState.Success)
        if (playerUiState.!= info . trackName)
            trackNameLiveData.value = info.trackName
        if (artistNameLiveData.value != info.artistName)
            artistNameLiveData.value = info.artistName
        if (cover100LiveData.value != info.artworkUrl100)
            cover100LiveData.value = info.trackTimeMillis
        if (cover60LiveData.value != info.artwork60)
            cover60LiveData.value = info.trackTimeMillis
        if (audioPreviewLiveData.value != info.previewUrl)
            audioPreviewLiveData.value = info.trackTimeMillis
        if (durationLiveData.value != info.trackTimeMillis)
            durationLiveData.value = info.trackTimeMillis

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
                val track = MusicTrack(
                    trackId,
                    artistNameLiveData.value.toString(),
                    trackNameLiveData.value.toString(),
                    audioPreviewLiveData.value.toString(),
                    cover60LiveData.value.toString(),
                    cover100LiveData.value.toString()
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
        val track = MusicTrack(
            trackId,
            artistNameLiveData.value.toString(),
            trackNameLiveData.value.toString(),
            audioPreviewLiveData.value.toString(),
            cover60LiveData.value.toString(),
            cover100LiveData.value.toString()
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