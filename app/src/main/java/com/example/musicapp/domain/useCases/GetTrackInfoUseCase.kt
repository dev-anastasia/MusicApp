package com.example.musicapp.domain.useCases

import com.example.musicapp.interfaces.OnGetTrackInfo

class GetTrackInfoUseCase(
    private val repos: OnGetTrackInfo
) {
    fun getTrackInfo(currentId: Long) : HashMap<String, String> {
        return repos.getTrackInfo(currentId)
    }
}