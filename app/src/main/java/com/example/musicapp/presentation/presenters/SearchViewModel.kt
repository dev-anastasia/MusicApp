package com.example.musicapp.presentation.presenters

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.SearchResultsListener
import com.example.musicapp.domain.entities.MusicPiece
import com.example.musicapp.presentation.ui.player.UIState

class SearchViewModel : ViewModel(), SearchResultsListener {

    private val searchUseCase = Creator.searchUseCase
    val uiState = MutableLiveData<UIState<Int>>()
    private val mainHandler = Handler(Looper.getMainLooper())

    val newList = MutableLiveData<List<MusicPiece>>()

    init {
        newList.value = emptyList()
    }

    override fun showEmptyResultsMessage() {
        uiState.value = UIState.Error
    }

    override fun onGetTracksListClicked(queryText: String, entity: String) {
        uiState.value = UIState.Loading
        searchUseCase.getSearchResult(queryText, entity) {
            mainHandler.post {
                if (it.isEmpty())
                    showEmptyResultsMessage()
                else
                    update(it)
            }
        }
    }

    override fun update(newList: List<MusicPiece>) {
        this.newList.value = newList
        uiState.value = UIState.Success
    }
}