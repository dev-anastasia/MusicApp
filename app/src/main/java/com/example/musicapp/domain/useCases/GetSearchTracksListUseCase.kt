package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.SearchResultsListener

class GetSearchTracksListUseCase(
    private val repo: SearchRepo,
    private var vm: SearchResultsListener? = null
) {

    fun getSearchResult(queryText: String, entity: String) {
        val list = repo.getSearchResult(queryText, entity)
        if (list.isEmpty().not())
            vm?.update(list)
        else
            vm?.showEmptyResultsMessage()
    }

    fun setVM(vm: SearchResultsListener) {
        this.vm = vm
    }
}