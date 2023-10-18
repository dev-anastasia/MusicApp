package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.SearchResultsListener
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.MusicPiece

class GetTrackListUseCase(
    private val repo: SearchRepo,
    private val vm: SearchResultsListener
) {

    private val queryResponseList: (List<MusicPiece>) -> Unit = {
        vm.update(it)
    }

    fun getSearchResult(queryText: String, entity: String) {
        repo.getSearchResult(queryText, entity, queryResponseList)
    }
}