package com.example.musicapp.domain.useCases

import android.content.Context
import com.example.musicapp.domain.FavTracksRepo
import com.example.musicapp.data.database.FavTrackEntity

class GetFavTracksListUseCase(private val repo: FavTracksRepo) {

    fun getAllFavTracks(context: Context) : List<FavTrackEntity> {
        return repo.getFavTracksList(context)
    }
}