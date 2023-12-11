package com.example.musicapp.domain

import com.example.musicapp.presentation.ui.player.SuccessTrackInfo

interface TrackInfoListener {

    fun updateTrackInfoIfServerRepliedSuccessfully(info: SuccessTrackInfo)
}