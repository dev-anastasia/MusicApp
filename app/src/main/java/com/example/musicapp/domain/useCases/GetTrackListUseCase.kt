package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.entities.MusicPiece

class GetTrackListUseCase(
    private val repos: SearchRepo
) {

    fun getData(queryText: String, entity: String): List<MusicPiece> {
        return repos.getData(queryText, entity)
    }
}