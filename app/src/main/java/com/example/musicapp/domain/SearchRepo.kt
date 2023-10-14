package com.example.musicapp.domain

import com.example.musicapp.domain.entities.MusicPiece

interface SearchRepo {

    fun getData(queryText: String, entity: String) : List<MusicPiece>
}