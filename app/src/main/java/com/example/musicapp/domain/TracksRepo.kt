package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.PlaylistWithTracks
import com.example.musicapp.domain.database.TrackEntity
import com.example.musicapp.domain.entities.MusicPiece

interface TracksRepo {

    fun getTracksIds(
        context: Context,
        playlistId: Int
    ): List<Long>

    fun getTracksInPlaylist(
        context: Context,
        trackId: Long,
        callback: (List<TrackEntity>) -> Unit
    )

    fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackEntity>) -> Unit
    )

    fun getSearchResult(
        queryText: String,
        entity: String,
        callback: (List<MusicPiece>) -> Unit
    )

    fun addPlaylistTrackRef(
        track: TrackEntity,
        ref: PlaylistTrackCrossRef,
        context: Context
    )

    fun findTrackInDB(
        playlistId: Int,
        trackId: Long,
        context: Context,
        callback: (List<Long>) -> Unit
    )
}