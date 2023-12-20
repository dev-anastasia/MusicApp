package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.ui.media.viewpager.TracksListUiState
import javax.inject.Inject

class TracksViewModel @Inject constructor(private val getTracksListUseCase: GetTracksListUseCase) :
    ViewModel() {

    val tracksList = MutableLiveData<List<MusicTrack>>(emptyList())
    val uiState = MutableLiveData<TracksListUiState<Int>>()

    fun getTracksList(playlistId: Int) {
        uiState.postValue(TracksListUiState.Loading)
        getTracksListUseCase.getPlaylistTracksList(playlistId) { list ->
            if (list.isEmpty()) {
                uiState.postValue(TracksListUiState.NoResults)
            } else {
                updateList(list)
                uiState.postValue(TracksListUiState.Success)
            }
        }
    }

    private fun updateList(list: List<MusicTrack>) {
        tracksList.postValue(list)
    }
}