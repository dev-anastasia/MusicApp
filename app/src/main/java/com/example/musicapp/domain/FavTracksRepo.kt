package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.data.database.FavTrackEntity
import com.example.musicapp.domain.entities.TrackInfo

interface FavTracksRepo {

    fun getFavTracksList(context: Context) : List<TrackInfo>
}