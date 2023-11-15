package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.domain.database.TrackEntity

interface GetTracksIdsRepo {

    fun getTracksIds(
        context: Context,
        playlistId: Int,
        callback: (List<Long>) -> Unit
    )

    fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackEntity>) -> Unit
    )
}