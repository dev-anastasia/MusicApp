package com.example.musicapp.domain

import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single

interface TracksRepo {

    fun getTracksIdsInSinglePlaylist(
        playlistId: Int
    ): Single<List<Long>>

    fun getTracksList(
        trackIdsList: List<Long>,
        callback: (List<MusicTrack>) -> Unit
    )

    fun getSearchResult(
        queryText: String
    ): Single<Music>

    fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
    ): Single<List<Long>>

    fun getPlaylistsOfThisTrack(
        trackId: Long,
        callback: (List<Int>) -> Unit
    )

    fun lookForTrackInMedia(
        trackId: Long
    ): Single<List<Long>>

    fun getTrackInfo(
        currentId: Long
    ): Single<TracksList>

    fun addTrackInPlaylist(
        track: MusicTrack,
        playlistId: Int
    )

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int
    )
}