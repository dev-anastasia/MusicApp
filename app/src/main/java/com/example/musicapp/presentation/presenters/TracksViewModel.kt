package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.ui.media.viewpager.TracksListUiState
import javax.inject.Inject

class TracksViewModel @Inject constructor(private val getTracksListUseCase: GetTracksListUseCase) :
    ViewModel() {

    val tracksList: LiveData<List<MusicTrack>>
        get() {
            return _tracksList
        }
    private val _tracksList = MutableLiveData<List<MusicTrack>>(emptyList())

    val uiState: MutableLiveData<TracksListUiState<Int>>
        get() {
            return _uiState
        }
    private val _uiState = MutableLiveData<TracksListUiState<Int>>()

    fun getTracksList(playlistId: Int) {
        uiState.postValue(TracksListUiState.Loading)
        getTracksListUseCase.getPlaylistTracksList(playlistId)
            .subscribe({ listOfTracksIds ->
                val list = getTracksListUseCase.getTracksList(listOfTracksIds)
                if (list.isEmpty()) {
                    _uiState.postValue(TracksListUiState.NoResults)
                } else {
                    _uiState.postValue(TracksListUiState.Success)
                }
                updateList(list)
            },
                { error ->
                    Log.e("RxJava", "TracksVM getTracksList fun problem: $error")
                }
            )
    }

    private fun updateList(list: List<MusicTrack>) {
        _tracksList.postValue(list)
    }
}