package com.example.musicapp.domain

import com.example.musicapp.presentation.ui.player.TrackInfoSpecs

interface TrackInfoListener {

    fun updateSuccessUiState(info: TrackInfoSpecs)
}