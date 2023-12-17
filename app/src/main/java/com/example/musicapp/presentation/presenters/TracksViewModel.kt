package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.presentation.ui.media.viewpager.TracksListUiState

class TracksViewModel : ViewModel() {

    val tracksList = MutableLiveData<List<MusicTrack>>(emptyList())
    val uiState = MutableLiveData<TracksListUiState<Int>>()

    fun getTracksList(playlistId: Int) {
        uiState.postValue(TracksListUiState.Loading)
        Creator.getTracksListUseCase.getPlaylistTracksList(playlistId) {
            updateList(it)
        }
    }

    private fun updateList(list: List<MusicTrack>) {
        tracksList.postValue(list)
        if (list.isEmpty()) {
            uiState.postValue(TracksListUiState.NoResults)
        } else {
            uiState.postValue(TracksListUiState.Success)
        }
    }
}