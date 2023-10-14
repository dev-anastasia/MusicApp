package com.example.musicapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.data.repos.TrackInfoRepos
import com.example.musicapp.domain.useCases.GetTrackInfoUseCase

class PlayerViewModel : ViewModel() {

    private val repos = TrackInfoRepos()

    val songNameLiveData: LiveData<String> = MutableLiveData()

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
        val specsMap = GetTrackInfoUseCase(repos).getTrackInfo(currentId)
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