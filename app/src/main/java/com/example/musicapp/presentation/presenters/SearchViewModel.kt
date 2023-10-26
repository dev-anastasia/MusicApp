package com.example.musicapp.presentation.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.Creator
import com.example.musicapp.domain.SearchResultsListener
import com.example.musicapp.domain.entities.MusicPiece

// Посчитала, что двум активити нужны разные ViewModel'и во имя соблюдения Single Responsibiity,
// а у разных фрагментов была бы одна ViewModel
class SearchViewModel : ViewModel(), SearchResultsListener {

    private val searchUseCase = Creator.searchUseCase

    val newList = MutableLiveData<List<MusicPiece>>()

    fun initList() {
        if (newList.value == null)
            newList.value = emptyList()
    }

    override fun showMessage() {
        TODO()
    }

    override fun onGetTrackListClicked(queryText: String, entity: String) {
        searchUseCase.getSearchResult(queryText, entity)
    }

    override fun update(newList: List<MusicPiece>) {
        this.newList.value = newList
    }
}