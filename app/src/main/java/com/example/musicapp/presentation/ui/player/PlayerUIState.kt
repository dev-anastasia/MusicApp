package com.example.musicapp.presentation.ui.player

import androidx.lifecycle.MutableLiveData

sealed class PlayerUIState<out Any> {

    object Success : PlayerUIState<SuccessSpecs>()

    object Error : PlayerUIState<Nothing>()
}

data class SuccessSpecs(
    val trackNameLiveData: MutableLiveData<String>,
    val artistNameLiveData: MutableLiveData<String>,
    val durationLiveData: MutableLiveData<String>,
    val audioPreviewLiveData: MutableLiveData<String>,
    val cover100LiveData: MutableLiveData<String>,  // Больший размер (для плеера)
    val cover60LiveData: MutableLiveData<String>   // Меньший размер (для БД)
)