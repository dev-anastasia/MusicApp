package com.example.musicapp.data

import androidx.lifecycle.MutableLiveData

sealed class ResponseState<out Int> {

    data class Success(
        val trackNameLiveData: MutableLiveData<String>,
        val artistNameLiveData: MutableLiveData<String>,
        val durationLiveData: MutableLiveData<String>,
        val audioPreviewLiveData: MutableLiveData<String>,
        val cover100LiveData: MutableLiveData<String>,  // Больший размер (для плеера)
        val cover60LiveData: MutableLiveData<String>   // Меньший размер (для БД)
    )

    object Error : ResponseState<Int>()
}