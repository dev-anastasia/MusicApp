package com.example.musicapp.domain

import android.content.Context
import com.example.musicapp.data.database.FavTrackEntity

interface TrackInfoRepo {

    fun getTrackInfo(currentId: Long, context: Context, callback: (HashMap<String, String>) -> Unit)

    fun addTrackToFavourites(context: Context, track: FavTrackEntity)

    fun deleteTrackFromFavourites(context: Context, id: Long)
}