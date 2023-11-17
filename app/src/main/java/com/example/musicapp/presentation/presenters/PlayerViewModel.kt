package com.example.musicapp.presentation.presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.presentation.ui.player.PlayerUIState

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val mainHandler = Handler(Looper.getMainLooper())

    val playerUiState = MutableLiveData<PlayerUIState<Int>>()

    val trackNameLiveData = MutableLiveData<String>()
    val artistNameLiveData = MutableLiveData<String>()
    val artworkUrl60LiveData = MutableLiveData<String>()
    val durationLiveData = MutableLiveData<String>()
    val audioPreviewLiveData = MutableLiveData<String>()
    val isLikedLiveData = MutableLiveData<Boolean>()
    val isAddedToMediaLiveData = MutableLiveData<Boolean>()
    var trackId: Long = 0

    init {
        trackNameLiveData.value = ""
        artistNameLiveData.value = ""
        artworkUrl60LiveData.value = ""
        durationLiveData.value = "0:00"
        audioPreviewLiveData.value = ""
        isLikedLiveData.value = false
        isAddedToMediaLiveData.value = false
    }

    fun getTrackInfoFromServer(currentId: Long, context: Context) {
        trackId = currentId

        Thread {
            Creator.getTrackInfoUseCase.getTrackInfo(trackId, context) {
                mainHandler.post {
                    updateLiveData(it)
                }
            }
        }.start()
    }

    private fun covertTrackDurationMillisToString() {
        if (durationLiveData.value!! != "0:00") {
            val dur = durationLiveData.value!!.toLong()
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()

            if (durationInSeconds.length < 2)
                durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"

            durationLiveData.value = "$durationInMinutes:$durationInSeconds"
        }
    }

    override fun updateLiveData(hashmap: HashMap<String, String>) {
        // Обновить данные лайвдаты согласно хэшмапе после ответа из сети

        // Перенести эти состояния(hashmap) в UIState,
        // здесь будет только лайвдата с uiState
        // liveData.value = UIState

        if (hashmap.isEmpty().not()) {
            if (trackNameLiveData.value != hashmap[TRACK_NAME])
                trackNameLiveData.value = hashmap[TRACK_NAME]
            if (artistNameLiveData.value != hashmap[ARTIST_NAME])
                artistNameLiveData.value = hashmap[ARTIST_NAME]
            if (artworkUrl60LiveData.value != hashmap[COVER_IMAGE])
                artworkUrl60LiveData.value = hashmap[COVER_IMAGE]
            if (audioPreviewLiveData.value != hashmap[PREVIEW])
                audioPreviewLiveData.value = hashmap[PREVIEW]
            if (durationLiveData.value != hashmap[DURATION])
                durationLiveData.value = hashmap[DURATION]

            if (playerUiState.value != PlayerUIState.Success)
                playerUiState.value = PlayerUIState.Success
        } else {
            trackNameLiveData.value = ""
            artistNameLiveData.value = ""
            artworkUrl60LiveData.value = ""
            audioPreviewLiveData.value = ""

            playerUiState.value = PlayerUIState.Error
        }
        covertTrackDurationMillisToString()
    }

    //Проверка статуса "Нравится"/"Не нравится" (иконка с сердечком)
    fun checkIfFavourite(context: Context) {
        Thread {
            Creator.getTracksListUseCase.findTrackInDB(-1, trackId, context) {
                mainHandler.post {
                    isLikedLiveData.value = it.isNotEmpty() // список непустой = true, в избранном
                }
            }
        }.start()
    }

    // Логика нажатия на кнопку "Нравится"
    fun likeClicked(context: Context, status: Boolean) {
        val track = TrackEntity(
            trackId,
            artistNameLiveData.value!!,
            trackNameLiveData.value!!,
            artworkUrl60LiveData.value!!,
            System.currentTimeMillis()
        )
        val crossRef = PlaylistTrackCrossRef(-1, trackId)
        if (status) {
            // добавляем в избранное
            Thread {
                Creator.insertTrackUseCase.addTrackToPlaylist(track, crossRef, context)
            }.start()
        } else {
            // удаляем из списка избранного
            Thread {

            }.start()
        }

        isLikedLiveData.value = status
    }

    // Логика нажатия на иконку "Медиа"
    fun addToMediaClicked(context: Context, playlistId: Int, status: Boolean) {
        if (status) {
            // добавляем в медиатеку
            Thread {
                val res = Creator.getPlaylistsUseCase.getAllPlaylists(context)
                if (res.isEmpty()) {

                }
            }.start()
        } else {
            // удаляем из медиатеки
        }
        isAddedToMediaLiveData.value = status
    }

    companion object {
        const val ARTIST_NAME = "artist name key"
        const val TRACK_NAME = "track name key"
        const val DURATION = "duration key"
        const val PREVIEW = "preview key"
        const val COVER_IMAGE = "cover image key"
    }
}