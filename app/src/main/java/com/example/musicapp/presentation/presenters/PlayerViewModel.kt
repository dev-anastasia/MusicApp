package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.presentation.OnItemClickListener
import com.example.musicapp.presentation.ui.adapter.MusicAdapter

class PlayerViewModel : ViewModel() {

    val songNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val artistNameLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val coverImageLinkLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val durationLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val previewLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val isLikedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isAddedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getTrackInfoClicked(currentId: Long) {
        val specsMap = Creator.trackInfoUseCase.getTrackInfo(currentId)
        if (specsMap.isEmpty().not()) {
            songNameLiveData.value
            artistNameLiveData.value = specsMap["artistName"]
            songNameLiveData.value = specsMap["trackName"]
            coverImageLinkLiveData.value = specsMap["cover"]
            durationLiveData.value = specsMap["duration"]

            val dur = specsMap["preview"]!!.toLong()
            // Длительность трека
            val durationInMinutes = (dur / 1000 / 60).toString()
            var durationInSeconds = (dur / 1000 % 60).toString()
            if (durationInSeconds.length < 2)
                durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"
            //duration.text = "$durationInMinutes:$durationInSeconds"
        }
    }
}