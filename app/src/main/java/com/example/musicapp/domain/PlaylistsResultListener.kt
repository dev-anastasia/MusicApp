package com.example.musicapp.domain

import com.example.musicapp.domain.entities.database.PlaylistEntity

interface PlaylistsResultListener {

    fun updateList(list: List<PlaylistEntity>)
}