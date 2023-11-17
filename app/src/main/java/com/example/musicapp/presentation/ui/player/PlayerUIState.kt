package com.example.musicapp.presentation.ui.player

sealed class PlayerUIState<out Int> {

    object Success : PlayerUIState<Nothing>()

    object Error : PlayerUIState<Nothing>()
}