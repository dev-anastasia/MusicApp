package com.example.musicapp.presentation

interface OnPlaylistClickListener {

    fun openPlaylistClicked(id: Int)

    fun deletePlaylistClicked(id: Int)

    fun getPlaylistTracksCount(playlistId: Int): Int

    fun getPlaylistCover(playlistId: Int): String?
}