package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.domain.FavTracksRepo
import com.example.musicapp.data.database.FavTrackEntity
import com.example.musicapp.data.database.PlaylistDatabase
import com.example.musicapp.domain.entities.TrackInfo

class FavTracksRepoImpl : FavTracksRepo {

    override fun getFavTracksList(context: Context) : List<TrackInfo> {
        return PlaylistDatabase.getDatabase(context).playlistsDao().getFavTracksList()
            .map {
                TrackInfo(
                    artistName = it.artistName,

                )
            }
    }
}