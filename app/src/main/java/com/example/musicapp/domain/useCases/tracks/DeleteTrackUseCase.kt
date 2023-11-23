package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo

class DeleteTrackUseCase(private val repo: TracksRepo) {

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    ) {
        repo.deleteTrackFromPlaylist(trackId, playlistId, context)
    }
}