package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.domain.GetTracksIdsRepo
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.TrackEntity

class GetTracksIdsRepoImpl : GetTracksIdsRepo {

    override fun getTracksIds(
        context: Context,
        playlistId: Int,
        callback: (List<Long>) -> Unit
    ) {
        val idsList = PlaylistDatabase.getDatabase(context).playlistsDao().getTracksIds(playlistId)
        callback(idsList)
    }

    override fun getTracksList(
        context: Context,
        trackIdsList: List<Long>,
        callback: (List<TrackEntity>) -> Unit
    ) {
        for (i in trackIdsList) {
            val tracksList =
                PlaylistDatabase.getDatabase(context).playlistsDao().getAllTracksList(i)
            callback(tracksList)
        }
    }
}