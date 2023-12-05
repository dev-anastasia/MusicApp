package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single

interface TracksRepo {

    fun getTracksIdsInSinglePlaylist(
        playlistId: Int,
        context: Context
    ): List<Long>

    fun getTracksList(
        trackIdsList: List<Long>,
        context: Context,
        callback: (List<MusicTrack>) -> Unit
    )

    fun getSearchResult(
        queryText: String,
        entity: String
    ): Single<Music>

    fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    ): List<Long>

    fun getPlaylistsOfThisTrack(
        trackId: Long,
        context: Context,
        callback: (List<Int>) -> Unit
    )

    fun findTrackInMedia(
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    )

    fun getTrackInfo(
        currentId: Long,
        context: Context
    ): Single<TracksList>

    fun addTrackInPlaylist(
        track: MusicTrack,
        playlistId: Int,
        context: Context
    )

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    )
}