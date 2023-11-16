package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.Creator.ARTIST_NAME
import com.example.musicapp.Creator.COVER_IMAGE
import com.example.musicapp.Creator.DURATION
import com.example.musicapp.Creator.PREVIEW
import com.example.musicapp.Creator.TRACK_NAME
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.database.PlaylistDatabase
import com.example.musicapp.domain.database.PlaylistTrackCrossRef
import com.example.musicapp.domain.entities.Tracks
import retrofit2.Call

class TrackInfoRepoImpl : TrackInfoRepo {

    override fun getTrackInfo(
        currentId: Long,
        context: Context,
        callback: (HashMap<String, String>) -> Unit
    ) {

        val mapOfSpecs = HashMap<String, String>()

        Thread {
            val searchObject: Call<Tracks> = RetrofitUtils.musicService.getTrackInfoById(currentId)
            val responseBody = searchObject.execute().body()

            if (responseBody != null) {
                if (responseBody.resultCount != 0) {
                    val res = responseBody.results[0]
                    mapOfSpecs[ARTIST_NAME] = res.artistName
                    mapOfSpecs[TRACK_NAME] = res.trackName
                    mapOfSpecs[DURATION] = res.trackTimeMillis.toString()
                    mapOfSpecs[PREVIEW] = res.previewUrl
                    mapOfSpecs[COVER_IMAGE] = res.artworkUrl100
                }
            }   // В остальных случаях вернётся пустая мапа
            callback(mapOfSpecs)
        }.start()
    }

    override fun addTrackToPlaylist(context: Context, crossRef: PlaylistTrackCrossRef) {
        PlaylistDatabase.getDatabase(context).playlistsDao().insertTrack(crossRef)
    }

    override fun deleteTrackFromFavourites(context: Context, trackId: Long, playlistId: Int) {
        PlaylistDatabase.getDatabase(context).playlistsDao().deleteTrack(trackId, playlistId)
    }
}