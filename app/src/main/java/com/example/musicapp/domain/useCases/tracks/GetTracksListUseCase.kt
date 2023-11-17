package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.PlaylistWithTracks
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.MusicPiece

class GetTracksListUseCase(private val repo: TracksRepo) {

    fun getTracksList(
        context: Context,
        playlistId: Int,
        callback: (List<TrackEntity>) -> Unit
    ) {
        val idsList = repo.getTracksIds(context, playlistId)
        for (i in idsList) {
            repo.getTracksInPlaylist(context, i, callback)
        }
    }

    fun getSearchResults(
        queryText: String,
        entity: String,
        callback: (List<MusicPiece>) -> Unit
    ) {
        repo.getSearchResult(queryText, entity, callback)
    }

    fun findTrackInDB(playlistId: Int,
                      trackId: Long,
                      context: Context,
                      callback: (List<Long>) -> Unit) {
        repo.findTrackInDB(playlistId, trackId, context, callback)
    }
}