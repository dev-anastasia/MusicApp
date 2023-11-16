package com.example.musicapp.domain

import com.example.musicapp.domain.entities.MusicPiece

interface SearchResultsListener {

    fun onGetTracksListClicked(queryText: String, entity: String)

    fun update(newList: List<MusicPiece>)

    fun showEmptyResultsMessage()
}