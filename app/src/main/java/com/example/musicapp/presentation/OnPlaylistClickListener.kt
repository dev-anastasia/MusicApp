package com.example.musicapp.presentation

interface OnPlaylistClickListener {

    fun openPlaylistClicked(id: Int)

    fun deletePlaylistClicked(id: Int)

    fun getPlaylistTracksCount(playlistId: Int, callback: (Int) -> Unit)

    fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit)
}