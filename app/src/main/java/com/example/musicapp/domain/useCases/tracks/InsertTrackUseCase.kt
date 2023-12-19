package com.example.musicapp.domain.useCases.tracks

import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.MusicTrack
import javax.inject.Inject

class InsertTrackUseCase @Inject constructor(val repo: TracksRepo) {

    fun addTrackToPlaylist(
        track: MusicTrack,
        playlistId: Int
    ) {
        repo.addTrackInPlaylist(track, playlistId)
    }
}