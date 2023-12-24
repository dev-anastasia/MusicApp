package com.example.musicapp.domain.useCases.tracks

import com.example.musicapp.domain.TracksRepo
import javax.inject.Inject

class DeleteTrackUseCase @Inject constructor(val repo: TracksRepo) {

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int
    ) {
        repo.deleteTrackFromPlaylist(trackId, playlistId)
    }
}