package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.MusicTrack

class InsertTrackUseCase(private val repo: TracksRepo) {

    fun addTrackToPlaylist(track: MusicTrack,
                           playlistId: Int,
                           context: Context) {
        repo.addTrackInPlaylist(track, playlistId, context)
    }
}