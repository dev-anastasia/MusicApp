package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.Creator.ARTIST_NAME
import com.example.musicapp.Creator.COVER_IMAGE
import com.example.musicapp.Creator.DURATION
import com.example.musicapp.Creator.PREVIEW
import com.example.musicapp.Creator.TRACK_NAME
import com.example.musicapp.domain.TrackInfoListener

class PlayerViewModel : ViewModel(), TrackInfoListener {

    private val useCase = Creator.trackInfoUseCase

    val trackNameLiveData = MutableLiveData<String>()
    val artistNameLiveData = MutableLiveData<String>()
    val coverImageLinkLiveData = MutableLiveData<String>()
    val durationLiveData = MutableLiveData<String>()
    val previewLiveData = MutableLiveData<String>()
    val isLikedLiveData = MutableLiveData<Boolean>()
    val isAddedLiveData = MutableLiveData<Boolean>()
    var serverReplied = false

    fun onGetTrackInfoClicked(currentId: Long) {
        useCase.getTrackInfo(currentId)
        countDuration()
    }



    private fun countDuration() {
        if (durationLiveData.value != null) {
            val dur = durationLiveData.value!!.toLong()
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()
            if (durationInSeconds.length < 2)
                durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"
            durationLiveData.value = "$durationInMinutes:$durationInSeconds"
        } else
            durationLiveData.value = "0:00"
    }

    override fun updateLD(hashmap: HashMap<String, String>) {
        // Обновить данные лайвдаты из мапы после ответа из сети

        trackNameLiveData.value = hashmap[TRACK_NAME]
        artistNameLiveData.value = hashmap[ARTIST_NAME]
        coverImageLinkLiveData.value = hashmap[COVER_IMAGE]
        previewLiveData.value = hashmap[PREVIEW]
        durationLiveData.value = hashmap[DURATION]

        serverReplied = true
    }

    fun updateIsLikedLD(status: Boolean) {
        isLikedLiveData.value = status
    }

    fun updateIsAddedLD(status: Boolean) {


        isAddedLiveData.value = status
    }

    fun initUserPrefsLD() {
        if (isLikedLiveData.value == null)
            isLikedLiveData.value = false
        if (isAddedLiveData.value == null)
            isAddedLiveData.value = false
    }

    fun onMediaClicked() {

    }
}