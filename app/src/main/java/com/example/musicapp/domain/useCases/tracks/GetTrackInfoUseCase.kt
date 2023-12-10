package com.example.musicapp.domain.useCases.tracks

import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single

class GetTrackInfoUseCase(private val repo: TracksRepo) {

    fun getTrackInfo(
        currentId: Long
    ): Single<TracksList> {
        return repo.getTrackInfo(currentId)
    }
}