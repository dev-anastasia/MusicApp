package com.example.musicapp.domain.useCases

import android.content.Context
import com.example.musicapp.domain.GetTracksIdsRepo
import com.example.musicapp.domain.database.TrackEntity

class GetTracksIdsListUseCase(private val repo: GetTracksIdsRepo) {

    fun getTracksIds(
        context: Context,
        playlistId: Int,
        callback: (List<Long>) -> Unit
    ) {
        repo.getTracksIds(context, playlistId, callback)
    }

    fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackEntity>) -> Unit
    ) {
        repo.getTracksList(context, trackIdsList, callback)
    }
}