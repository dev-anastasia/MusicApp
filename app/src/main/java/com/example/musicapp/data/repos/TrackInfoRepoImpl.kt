package com.example.musicapp.data.repos

import com.example.musicapp.Creator.ARTIST_NAME
import com.example.musicapp.Creator.COVER_IMAGE
import com.example.musicapp.Creator.DURATION
import com.example.musicapp.Creator.PREVIEW
import com.example.musicapp.Creator.TRACK_NAME
import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.entities.Tracks
import retrofit2.Call

class TrackInfoRepoImpl : TrackInfoRepo {

    override fun getTrackInfo(currentId: Long): HashMap<String, String> {

        val mapOfSpecs = HashMap<String, String>()
        lateinit var responseBody: Tracks

        val thread = Thread {
            val searchObject: Call<Tracks> = RetrofitUtils.musicService.getTrackInfoById(currentId)
            responseBody = searchObject.execute().body()!!
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
}