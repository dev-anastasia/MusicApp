package com.example.musicapp.presentation.presenters.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.presenters.SearchViewModel
import javax.inject.Inject

class SearchVMFactory @Inject constructor(private val getTracksListUseCase: GetTracksListUseCase) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(getTracksListUseCase) as T
    }
}