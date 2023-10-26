package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.SearchResultsListener
import com.example.musicapp.domain.entities.MusicPiece

class GetTrackListUseCase(
    private val repo: SearchRepo,
    private var vm: SearchResultsListener? = null
) {

    private fun queryResponseList(list: List<MusicPiece>) {
        vm?.update(list)
    }

    fun getSearchResult(queryText: String, entity: String) {
        val list = repo.getSearchResult(queryText, entity)
        if (list.isEmpty().not())
            queryResponseList(list)
        else
            vm?.showMessage()
    }

    fun setVM(vm: SearchResultsListener) {
        this.vm = vm
    }
}