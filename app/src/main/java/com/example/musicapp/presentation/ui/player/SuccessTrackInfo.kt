package com.example.musicapp.presentation.ui.player

import androidx.lifecycle.MutableLiveData


data class SuccessTrackInfo(
    var trackName: MutableLiveData<String>,
    var artistName: MutableLiveData<String>,
    var audioPreview: MutableLiveData<String>,
    var artworkUrl100: MutableLiveData<String>,  // Больший размер (для плеера)
    var artworkUrl60: MutableLiveData<String>  // Меньший размер (для БД)
)
