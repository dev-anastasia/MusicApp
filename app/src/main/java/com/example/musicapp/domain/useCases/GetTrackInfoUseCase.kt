package com.example.musicapp.domain.useCases

import android.content.Context
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.data.database.FavTrackEntity

class GetTrackInfoUseCase(
    private val repo: TrackInfoRepo,
    private var vm: TrackInfoListener? = null
) {

    fun getTrackInfo(currentId: Long, context: Context) {
        val hashmap = repo.getTrackInfo(currentId, context)
        vm?.updateLD(hashmap)
    }

    fun addTrackToFavourites(context: Context, track: FavTrackEntity) {
        repo.addTrackToFavourites(context, track)
    }

    fun deleteTrackFromFavourites(context: Context, id: Long) {
        repo.deleteTrackFromFavourites(context, id)
    }

    fun setVM(vm: TrackInfoListener) {
        this.vm = vm
    }
}