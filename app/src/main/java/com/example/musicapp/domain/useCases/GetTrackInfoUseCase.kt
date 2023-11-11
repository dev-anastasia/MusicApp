package com.example.musicapp.domain.useCases

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.data.database.FavTrackEntity

class GetTrackInfoUseCase(
    private val repo: TrackInfoRepo,
    private var vm: TrackInfoListener? = null
) {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun getTrackInfo(currentId: Long, context: Context) {
        repo.getTrackInfo(currentId, context) {
            mainHandler.post {
                vm?.updateLD(it)
            }
        }
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