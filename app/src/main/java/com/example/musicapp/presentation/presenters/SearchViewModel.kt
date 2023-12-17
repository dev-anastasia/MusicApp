package com.example.musicapp.presentation.presenters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.Creator
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.presentation.ui.search.SearchUIState
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

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
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
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