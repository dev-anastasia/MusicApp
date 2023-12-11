package com.example.musicapp.presentation.ui.player

import androidx.lifecycle.MutableLiveData

sealed class PlayerUIState<out Int> {

    object Success : PlayerUIState<Nothing>()

    object Error : PlayerUIState<Nothing>()
}