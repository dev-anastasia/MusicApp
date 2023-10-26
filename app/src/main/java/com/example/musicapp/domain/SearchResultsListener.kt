package com.example.musicapp.domain

import com.example.musicapp.domain.entities.MusicPiece

interface SearchResultsListener {

    fun onGetTrackListClicked(queryText: String, entity: String)

    fun update(newList: List<MusicPiece>)

    fun showMessage()
}