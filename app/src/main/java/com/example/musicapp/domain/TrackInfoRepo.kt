package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.database.TrackEntity

interface TrackInfoRepo {

    fun getTrackInfo(currentId: Long, context: Context, callback: (HashMap<String, String>) -> Unit)

    fun addTrackToPlaylist(context: Context, crossRef: PlaylistTrackCrossRef)

    fun deleteTrackFromFavourites(context: Context, trackId: Long, playlistId: Int)
}