package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.database.TrackTable
import com.example.musicapp.domain.entities.MusicTrack

class GetTracksListUseCase(private val repo: TracksRepo) {

    fun getTracksList(
        context: Context,
        playlistId: Int,
        callback: (List<TrackTable>) -> Unit
    ) {
        val idsList = repo.getTracksIdsInSinglePlaylist(context, playlistId)
        repo.getTracksList(context, idsList, callback)
    }

    fun getSearchResults(
        queryText: String,
        entity: String,
        callback: (List<MusicTrack>) -> Unit
    ) {
        repo.getSearchResult(queryText, entity, callback)
    }

    fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        repo.findTrackInSinglePlaylist(trackId, playlistId, context, callback)
    }

    fun lookForTrackInMedia(
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        repo.findTrackInMedia(trackId, context, callback)
    }
}