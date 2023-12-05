package com.example.musicapp.domain.useCases.tracks

import android.content.Context
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import io.reactivex.Single

class GetTracksListUseCase(private val repo: TracksRepo) {

    fun getPlaylistTracksList(
        context: Context,
        playlistId: Int,
        callback: (List<MusicTrack>) -> Unit
    ) {
        val idsList = repo.getTracksIdsInSinglePlaylist(playlistId, context)
        repo.getTracksList(idsList, context, callback)
    }

    fun getSearchResults(
        queryText: String,
        entity: String
    ): Single<Music> {
        return repo.getSearchResult(queryText, entity)
    }

    fun lookForTrackInPlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        callback(repo.findTrackInSinglePlaylist(trackId, playlistId, context))
    }

    fun lookForTrackInMedia(
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    ) {
        repo.findTrackInMedia(trackId, context, callback)
    }
}