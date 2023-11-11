package com.example.musicapp.domain

import com.example.musicapp.data.database.PlaylistEntity

interface PlaylistsResultListener {

    fun updateList(list: List<PlaylistEntity>)
}