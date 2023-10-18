package com.example.musicapp.domain.useCases

import com.example.musicapp.domain.TrackInfoRepo

class GetTrackInfoUseCase(
    private val repos: TrackInfoRepo
) {
    fun getTrackInfo(currentId: Long) : HashMap<String, String> {
        return repos.getTrackInfo(currentId)
    }
}