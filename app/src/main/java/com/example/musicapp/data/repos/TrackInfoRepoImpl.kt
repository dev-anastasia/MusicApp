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

//                    songName.text = results.trackName
//                    artistName.text = results.artistName
//
//                    playIcon.isClickable = true
//                    favIcon.isClickable = true
//                    mediaIcon.isClickable = true
//
//                    Picasso.get()
//                        .load(Uri.parse(results.artworkUrl100))
//                        .placeholder(R.drawable.note_placeholder)
//                        .into(findViewById<ImageView>(R.id.player_activity_iv_cover))

        // Длительность трека
//                    val durationInMinutes = (results.trackTimeMillis / 1000 / 60).toString()
//                    var durationInSeconds = (results.trackTimeMillis / 1000 % 60).toString()
//                    if (durationInSeconds.length < 2)
//                        durationInSeconds = "0$durationInSeconds"   // вместо "1:7" -> "1:07"
//                    duration.text = "$durationInMinutes:$durationInSeconds"

        // Инициализация значений ViewModel:
//                    playerViewModel.coverImageLinkLiveData.value = results.artworkUrl100
//                    playerViewModel.songNameLiveData.value = results.trackName
//                    playerViewModel.artistNameLiveData.value = results.artistName
//                    playerViewModel.durationLiveData.value = duration.text.toString()

//                    playIcon.setImageResource(R.drawable.icon_play_disabled)
//                    favIcon.setImageResource(R.drawable.icon_fav_empty)
//                    mediaIcon.setImageResource(R.drawable.icon_media_empty)
//
//                    Toast.makeText(
//                        this@PlayerActivity,
//                        "Ошибка: не удалось связаться с сервером",
//                        Toast.LENGTH_SHORT
//                    ).show()

        return mapOfSpecs
    }
}