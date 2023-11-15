package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.SearchResultsListener
import com.example.musicapp.domain.entities.MusicPiece

class SearchViewModel : ViewModel(), SearchResultsListener {

    private val searchUseCase = Creator.searchUseCase

    val newList = MutableLiveData<List<MusicPiece>>()

    init {
        newList.value = emptyList()
    }

    override fun showEmptyResultsMessage() {
        println("")
    }

    override fun onGetTrackListClicked(queryText: String, entity: String) {
        searchUseCase.getSearchResult(queryText, entity)
    }

    override fun update(newList: List<MusicPiece>) {
        this.newList.value = newList
    }
}