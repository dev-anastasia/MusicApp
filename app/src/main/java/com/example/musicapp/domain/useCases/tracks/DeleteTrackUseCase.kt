package com.example.musicapp.domain.useCases.tracks

import com.example.musicapp.domain.TracksRepo

class DeleteTrackUseCase(private val repo: TracksRepo) {

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int
    ) {
        repo.deleteTrackFromPlaylist(trackId, playlistId)
    }
}