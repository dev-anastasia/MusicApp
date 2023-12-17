package com.example.musicapp.presentation.ui.media.viewpager

sealed class TracksListUiState<out Int> {

    object Success : TracksListUiState<Nothing>()

    object Loading : TracksListUiState<Nothing>()

    object NoResults : TracksListUiState<Nothing>()
}