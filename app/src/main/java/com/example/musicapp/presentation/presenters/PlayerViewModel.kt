package com.example.musicapp.presentation.presenters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.Creator.ARTIST_NAME
import com.example.musicapp.Creator.COVER_IMAGE
import com.example.musicapp.Creator.DURATION
import com.example.musicapp.Creator.PREVIEW
import com.example.musicapp.Creator.TRACK_NAME
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.database.PlaylistTrackCrossRef

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val trackInfoUseCase = Creator.trackInfoUseCase

    val trackNameLiveData = MutableLiveData<String>()
    val artistNameLiveData = MutableLiveData<String>()
    val coverImageLinkLiveData = MutableLiveData<String>()
    val durationLiveData = MutableLiveData<String>()
    val previewLiveData = MutableLiveData<String>()
    val isLikedLiveData = MutableLiveData<Boolean>()
    val isAddedLiveData = MutableLiveData<Boolean>()
    val serverReplied = MutableLiveData<Boolean>()
    var trackId: Long = 0

    init {
        trackNameLiveData.value = ""
        artistNameLiveData.value = ""
        coverImageLinkLiveData.value = ""
        durationLiveData.value = ""
        previewLiveData.value = ""
        isLikedLiveData.value = false
        isAddedLiveData.value = false
        serverReplied.value = false
    }

    fun getTrackInfoFromServer(currentId: Long, context: Context) {
        trackId = currentId
        trackInfoUseCase.getTrackInfo(currentId, context)
        countDuration()
    }

    private fun countDuration() {
        if (durationLiveData.value != null) {
            if (durationLiveData.value!!.isEmpty().not()) {
                val dur = durationLiveData.value!!.toLong()
                val durationInMinutes = (dur / 1000 / 60).toString()
                var durationInSeconds = (dur / 1000 % 60).toString()
                if (durationInSeconds.length < 2)
                    durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"
                durationLiveData.value = "$durationInMinutes:$durationInSeconds"
            } else
                durationLiveData.value = "0:00"
        } else
            durationLiveData.value = "0:00"
    }

    override fun updateLiveData(hashmap: HashMap<String, String>) {
        // Обновить данные лайвдаты из мапы после ответа из сети

        // Перенести эти состояния(hashmap) в UIState,
        // здесь будет только лайвдата с uiState
        // liveData.value = UIState
        //
        if (hashmap.isEmpty().not()) {
            trackNameLiveData.value = hashmap[TRACK_NAME]
            artistNameLiveData.value = hashmap[ARTIST_NAME]
            coverImageLinkLiveData.value = hashmap[COVER_IMAGE]
            previewLiveData.value = hashmap[PREVIEW]
            durationLiveData.value = hashmap[DURATION]
        } else {
            trackNameLiveData.value = ""
            artistNameLiveData.value = ""
            coverImageLinkLiveData.value = ""
            previewLiveData.value = ""
            durationLiveData.value = ""
        }
        serverReplied.value = true
    }

    // Логика нажатие на кнопку с сердечком
    fun updateIsLikedLD(context: Context, status: Boolean) {
        if (status) {
            // добавить в избранное
            val crossRef = PlaylistTrackCrossRef(
                0,
                trackId
            )
            val thread = Thread {
                trackInfoUseCase.addTrackToPlaylist(context, crossRef)
            }
            thread.start()
        } else {
            // удалить из списка избранного
            val thread = Thread {
                trackInfoUseCase.deleteTrackFromFavourites(context, trackId, 0)
            }
            thread.start()
        }

        isLikedLiveData.value = status
    }

    // Логика нажатия на иконку "Медиа"
    fun updateIsAddedLD(status: Boolean) {
        // добавить в Медиа / удалить из Медиа
        isAddedLiveData.value = status
    }
}