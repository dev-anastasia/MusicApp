package com.example.musicapp.presentation

interface OnPlaylistClickListener {

    var currId: Int

    fun openPlaylistClicked(id: Int)

    fun deletePlaylistClicked()

}