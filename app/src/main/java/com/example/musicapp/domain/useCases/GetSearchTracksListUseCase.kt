package com.example.musicapp.domain.useCases

import android.os.Handler
import android.os.Looper
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.MusicPiece

class GetSearchTracksListUseCase(private val repo: SearchRepo) {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun getSearchResult(queryText: String, entity: String, callback: (List<MusicPiece>) -> Unit) {
        repo.getSearchResult(queryText, entity) {
            mainHandler.post {
                callback(it)
            }
        }
    }

//    fun setVM(vm: SearchResultsListener) {
//        this.vm = vm
//    }
}