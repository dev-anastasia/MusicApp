package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackTable
import com.example.musicapp.domain.entities.MusicTrack

interface TracksRepo {

    fun getTracksIdsInSinglePlaylist(
        context: Context,
        playlistId: Int
    ): List<Long>

    fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackTable>) -> Unit
    )

    fun getSearchResult(
        queryText: String,
        entity: String,
        callback: (List<MusicTrack>) -> Unit
    )

    fun findTrackInSinglePlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context,
        callback: (List<Long>) -> Unit
    )

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
        context: Context,
        callback: (HashMap<String, String>) -> Unit
    )

    fun addTrackInPlaylist(
        track: TrackTable,
        ref: PlaylistTrackCrossRef,
        context: Context
    )

    fun deleteTrackFromPlaylist(
        trackId: Long,
        playlistId: Int,
        context: Context
    )
}