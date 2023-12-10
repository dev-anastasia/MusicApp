package com.example.musicapp.presentation

import io.reactivex.Single

interface OnPlaylistClickListener {

    fun openPlaylistClicked(id: Int)

    fun deletePlaylistClicked(id: Int)

    fun getPlaylistTracksCount(playlistId: Int): Single<List<Long>>

    fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit)
}