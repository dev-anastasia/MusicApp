package com.example.musicapp.interfaces

interface OnSearchListener {

    fun onGetTrackListClicked(
        listener: OnItemClickListener,
        queryText: String,
        entity: String
    )
}