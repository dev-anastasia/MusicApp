package com.example.musicapp.presentation.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.entities.TrackInfo

sealed class PlayerUIState<out Int> {

    object Success : PlayerUIState<Nothing>() {

        val data: LiveData<SuccessTrackInfo>
            get() {
                return _data
            }
        private val _data = MutableLiveData<SuccessTrackInfo>()

        data class SuccessTrackInfo(
            val trackName: MutableLiveData<String>,
            val artistName: MutableLiveData<String>,
            val audioPreview: MutableLiveData<String>,
            val durationString: MutableLiveData<String>,
            val artworkUrl100: MutableLiveData<String>,  // Больший размер (для плеера)
            val artworkUrl60: MutableLiveData<String>  // Меньший размер (для БД)
        )

        fun updateData(res: TrackInfo) {
            if (data.value?.trackName?.value != res.trackName) {
                _data.value?.trackName?.postValue(res.trackName)
            }
            if (data.value?.artistName?.value != res.artistName) {
                _data.value?.artistName?.postValue(res.artistName)
            }
            if (data.value?.artworkUrl100?.value != res.artworkUrl100) {
                _data.value?.artworkUrl100?.postValue(res.artworkUrl100)
            }
            if (data.value?.artworkUrl60?.value != res.artworkUrl60) {
                _data.value?.artworkUrl60?.postValue(res.artworkUrl60)
            }
            if (data.value?.audioPreview?.value != res.previewUrl) {
                _data.value?.audioPreview?.postValue(res.previewUrl)
            }
        }
    }

    object Error : PlayerUIState<Nothing>()
}