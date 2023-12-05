package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single

class GetTrackInfoUseCase(private val repo: TracksRepo) {

    fun getTrackInfo(
        currentId: Long,
        context: Context
    ): Single<TracksList> {
        return repo.getTrackInfo(currentId, context)
    }
}