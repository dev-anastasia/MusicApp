package com.example.musicapp.domain.useCases

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.database.PlaylistTrackCrossRef

class GetTrackInfoUseCase(private val repo: TrackInfoRepo) {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun getTrackInfo(
        currentId: Long,
        context: Context,
        callback: (HashMap<String, String>) -> Unit
    ) {
        repo.getTrackInfo(currentId, context) {
            mainHandler.post {
                callback(it)
            }
        }
    }

    fun addTrackToPlaylist(context: Context, crossRef: PlaylistTrackCrossRef) {
        repo.addTrackToPlaylist(context, crossRef)
    }

    fun deleteTrackFromFavourites(context: Context, trackId: Long, playlistId: Int) {
        repo.deleteTrackFromFavourites(context, trackId, playlistId)
    }
}