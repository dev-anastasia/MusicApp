package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackEntity

class InsertTrackUseCase(private val repo: TracksRepo) {

    fun addTrackToPlaylist(track: TrackEntity, ref: PlaylistTrackCrossRef, context: Context) {
        repo.addPlaylistTrackRef(track, ref, context)
    }
}