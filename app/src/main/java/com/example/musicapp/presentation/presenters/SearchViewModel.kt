package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.presentation.ui.search.SearchUIState
import io.reactivex.android.schedulers.AndroidSchedulers

class SearchViewModel : ViewModel() {

    val searchUiState = MutableLiveData<SearchUIState<Int>>()
    val searchResultsList = MutableLiveData<List<MusicTrack>>(emptyList())

    fun onGetTracksListClicked(queryText: String) {

        searchUiState.postValue(SearchUIState.Loading)

        Creator.getTracksListUseCase.getSearchResults(queryText)
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                if (result.resultCount == 0) {
                    searchUiState.postValue(SearchUIState.NoResults)
                } else {
                    updateResultsList(result.results)
                }
            }, { error ->
                Log.e("RxJava", "getSearchResults fun problem: $error")
            })
    }

    private fun updateResultsList(newList: List<MusicTrack>) {
        searchResultsList.postValue(newList)
        searchUiState.postValue(SearchUIState.Success)
    }
}