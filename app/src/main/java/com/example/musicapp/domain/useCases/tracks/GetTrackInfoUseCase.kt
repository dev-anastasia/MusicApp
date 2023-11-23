package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo

class GetTrackInfoUseCase(private val repo: TracksRepo) {

    fun getTrackInfo(
        currentId: Long,
        context: Context,
        callback: (HashMap<String, String>) -> Unit
    ) {
        repo.getTrackInfo(currentId, context) {
            callback(it)
        }
    }
}