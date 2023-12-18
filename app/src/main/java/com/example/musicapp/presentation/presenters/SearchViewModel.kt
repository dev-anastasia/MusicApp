package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.ui.search.SearchUIState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    usecase: GetTracksListUseCase
) : ViewModel() {

    val searchUiState: LiveData<SearchUIState<Int>>
        get() {
            return _searchUiState
        }
    private val _searchUiState = MutableLiveData<SearchUIState<Int>>()

    val searchResultsList: LiveData<List<MusicTrack>>
        get() {
            return _searchResultsList
        }
    private val _searchResultsList = MutableLiveData<List<MusicTrack>>(emptyList())

    fun onGetTracksListClicked(queryText: String) {

        _searchUiState.postValue(SearchUIState.Loading)

        Creator.getTracksListUseCase.getSearchResults(queryText)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                if (result == null) {
                    _searchUiState.postValue(SearchUIState.Error)
                    return@subscribe
                }
                if (result.resultCount == 0) {
                    _searchUiState.postValue(SearchUIState.NoResults)
                } else {
                    updateResultsList(result.results)
                }
            }, { error ->
                Log.e("RxJava", "getSearchResults fun problem: $error")
            })
    }

    private fun updateResultsList(newList: List<MusicTrack>) {
        _searchResultsList.postValue(newList)
        _searchUiState.postValue(SearchUIState.Success)
    }
}