package com.example.musicapp.domain.useCases

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.musicapp.domain.TrackInfoListener
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.database.PlaylistTrackCrossRef

class GetTrackInfoUseCase(
    private val repo: TrackInfoRepo,
    private var vm: TrackInfoListener? = null
) {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun getTrackInfo(currentId: Long, context: Context) {
        repo.getTrackInfo(currentId, context) {
            mainHandler.post {
                vm?.updateLiveData(it)
            }
        }
    }

    fun addTrackToPlaylist(context: Context, crossRef: PlaylistTrackCrossRef) {
        repo.addTrackToPlaylist(context, crossRef)
    }

    fun deleteTrackFromFavourites(context: Context, trackId: Long, playlistId: Int) {
        repo.deleteTrackFromFavourites(context, trackId, playlistId)
    }

    fun setVM(vm: TrackInfoListener) {
        this.vm = vm
    }
}