package com.example.musicapp.presentation.presenters

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.presentation.ui.search.SearchUIState

class SearchViewModel : ViewModel() {

    val searchUiState = MutableLiveData<SearchUIState<Int>>()
    val searchResultsList = MutableLiveData<List<MusicTrack>>()
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        searchResultsList.value = emptyList()
    }

    fun onGetTracksListClicked(queryText: String, entity: String) {
        searchUiState.value = SearchUIState.Loading
        Creator.getTracksListUseCase.getSearchResults(queryText, entity) {
            mainHandler.post {
                if (it.isEmpty())
                    showEmptyResultsMessage()
                else
                    update(it)
            }
        }
    }

    private fun showEmptyResultsMessage() {
        searchUiState.value = SearchUIState.Error
    }

    private fun update(newList: List<MusicTrack>) {
        this.searchResultsList.value = newList
        searchUiState.value = SearchUIState.Success
    }
}