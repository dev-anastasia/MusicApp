package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.domain.FavTracksRepo
import com.example.musicapp.data.database.FavTrackEntity
import com.example.musicapp.data.database.PlaylistDatabase

class FavTracksRepoImpl : FavTracksRepo {

    override fun getFavTracksList(context: Context) : List<FavTrackEntity> {
        return PlaylistDatabase.getDatabase(context).playlistsDao().getFavTracksList()
    }
}