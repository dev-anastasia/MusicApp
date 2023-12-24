package com.example.musicapp.domain.useCases.tracks

import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import io.reactivex.Single
import javax.inject.Inject

class GetTracksListUseCase @Inject constructor(val repo: TracksRepo) {

    fun getPlaylistTracksList(
        playlistId: Int
    ): Single<List<Long>> {
        return repo.getTracksIdsInSinglePlaylist(playlistId)
    }

    fun getTracksList(listOfTracksIds: List<Long>): List<MusicTrack> {
        return repo.getTracksList(listOfTracksIds)
    }

    fun getSearchResults(
        queryText: String
    ): Single<Music> {
        return repo.getSearchResult(queryText)
    }

    fun lookForTrackInPlaylist(
        trackId: Long,
        playlistId: Int
    ): Single<List<Long>> {
        return repo.findTrackInSinglePlaylist(trackId, playlistId)
    }

    fun lookForTrackInMedia(
        trackId: Long
    ): Single<List<Long>> {
        return repo.lookForTrackInMedia(trackId)
    }
}