package com.example.musicapp.presentation.ui.search

sealed class SearchUIState<out Int> {

    object Success : SearchUIState<Nothing>()

    object Loading : SearchUIState<Nothing>()

    object NoResults : SearchUIState<Nothing>()

    object Error : SearchUIState<Nothing>()
}