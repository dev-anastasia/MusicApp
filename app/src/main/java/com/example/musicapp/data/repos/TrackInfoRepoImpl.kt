package com.example.musicapp.data.repos

import android.content.Context
import com.example.musicapp.Creator.ARTIST_NAME
import com.example.musicapp.Creator.COVER_IMAGE
import com.example.musicapp.Creator.DURATION
import com.example.musicapp.Creator.MEDIA_STATUS
import com.example.musicapp.Creator.PREVIEW
import com.example.musicapp.Creator.TRACK_NAME
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.entities.Tracks
import com.example.musicapp.data.database.FavTrackEntity
import com.example.musicapp.data.database.PlaylistDatabase
import retrofit2.Call

class TrackInfoRepoImpl : TrackInfoRepo {

    override fun getTrackInfo(currentId: Long, context: Context): HashMap<String, String> {

        val mapOfSpecs = HashMap<String, String>()
        lateinit var responseBody: Tracks
        var mediaStatus = false

        val thread = Thread {
            val searchObject: Call<Tracks> = RetrofitUtils.musicService.getTrackInfoById(currentId)
            responseBody = searchObject.execute().body()!!

            //mediaStatus = getMediaStatusFromDatabase(currentId, context)
        }
        thread.apply {
            start()
            join()
        }

        val body = responseBody.results[0]
        mapOfSpecs[ARTIST_NAME] = body.artistName
        mapOfSpecs[TRACK_NAME] = body.trackName
        mapOfSpecs[DURATION] = body.trackTimeMillis.toString()
        mapOfSpecs[PREVIEW] = body.previewUrl
        mapOfSpecs[COVER_IMAGE] = body.artworkUrl100

        return mapOfSpecs
    }

    // Ниже - неудачная попытка работать со статусами "Избранное" и "Добавлено в Медиа"
    //private fun getMediaStatusFromDatabase(id: Long, context: Context) : Boolean {
      //  return PlaylistDatabase.getDatabase(context).playlistsDao().getMediaStatus(id)
    //}

    override fun addTrackToFavourites(context: Context, track: FavTrackEntity) {
        PlaylistDatabase.getDatabase(context).playlistsDao().insertFavTrack(track)
    }

    override fun deleteTrackFromFavourites(context: Context, id: Long) {
        PlaylistDatabase.getDatabase(context).playlistsDao().deleteFavTrack(id)
    }
}