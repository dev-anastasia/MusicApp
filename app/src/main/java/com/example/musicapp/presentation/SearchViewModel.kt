package com.example.musicapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.data.repos.SearchRepoImpl
import com.example.musicapp.domain.entities.MusicPiece
import com.example.musicapp.domain.useCases.GetTrackListUseCase
import com.example.musicapp.interfaces.OnItemClickListener
import com.example.musicapp.interfaces.OnSearchListener

// Посчитала, что двум активити нужны разные ViewModel'и во имя соблюдения Single Responsibiity,
// а у разных фрагментов была бы одна ViewModel
class SearchViewModel : ViewModel(), OnSearchListener {

    private val repos = SearchRepoImpl()

    val newListLiveData = MutableLiveData<List<MusicPiece>>()

    val ld = MutableLiveData<MusicPiece>()

    private var oldList = emptyList<MusicPiece>()
    private val getDataUseCase = GetTrackListUseCase(repos)

    override fun onGetTrackListClicked(
        listener: OnItemClickListener,
        queryText: String,
        entity: String
    ) {
        oldList = newListLiveData.value!!
        newListLiveData.value = getDataUseCase.getData(queryText, entity)
    }

}