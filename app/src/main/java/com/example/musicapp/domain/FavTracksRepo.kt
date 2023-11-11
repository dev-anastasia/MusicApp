package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.data.database.FavTrackEntity

interface FavTracksRepo {

    fun getFavTracksList(context: Context) : List<FavTrackEntity>
}