package com.example.musicapp.presentation.ui.media.viewpager

sealed class PlaylistsListUiState<out Int> {

    object Success : PlaylistsListUiState<Nothing>()

    object Loading : PlaylistsListUiState<Nothing>()

    object NoResults : PlaylistsListUiState<Nothing>()
}