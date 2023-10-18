package com.example.musicapp.domain

import com.example.musicapp.domain.entities.MusicPiece

interface SearchRepo {

    fun getSearchResult(
        queryText: String,
        entity: String,
        dataListener: (List<MusicPiece>) -> Unit
    )
}