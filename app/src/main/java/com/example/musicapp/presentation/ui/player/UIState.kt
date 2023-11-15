package com.example.musicapp.presentation.ui.player

sealed class UIState<out Int> {

    object Success : UIState<Nothing>()

    object Loading : UIState<Nothing>()

    object Error : UIState<Nothing>()
}