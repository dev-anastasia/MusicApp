package com.example.musicapp.presentation.presenters.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.presenters.TracksViewModel
import javax.inject.Inject

class TracksVMFactory @Inject constructor(
    private val getTracksListUseCase: GetTracksListUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TracksViewModel(
            getTracksListUseCase
        ) as T
    }
}