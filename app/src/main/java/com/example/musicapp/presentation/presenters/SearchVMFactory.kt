package com.example.musicapp.presentation.presenters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase

class SearchVMFactory(private val getTracksListUseCase: GetTracksListUseCase)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(getTracksListUseCase) as T
    }
}