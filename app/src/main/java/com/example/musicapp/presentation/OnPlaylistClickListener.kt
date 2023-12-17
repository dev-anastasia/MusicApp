package com.example.musicapp.presentation

import com.example.musicapp.domain.entities.PlaylistInfo

interface OnPlaylistClickListener {

    fun openPlaylistClicked(id: Int)

    fun deletePlaylistClicked(id: Int)

    fun getPlaylistInfo(playlistId: Int, callback: (PlaylistInfo) -> Unit)
}