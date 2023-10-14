package com.example.musicapp.data.repos

import com.example.musicapp.data.network.RetrofitUtils
import com.example.musicapp.domain.entities.Tracks
import com.example.musicapp.interfaces.OnGetTrackInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackInfoRepos : OnGetTrackInfo {

    override fun getTrackInfo(currentId: Long) : HashMap<String, String> {

        var mapOfSpecs = HashMap<String, String>()

        RetrofitUtils.musicService.getTrackInfoById(currentId)
            .enqueue(object : Callback<Tracks> {

                override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                    val body = response.body()!!.results[0]
                    mapOfSpecs["artistName"] = body.artistName
                    mapOfSpecs["trackName"] = body.trackName
                    mapOfSpecs["duration"] = body.trackTimeMillis.toString()
                    mapOfSpecs["preview"] = body.previewUrl
                    mapOfSpecs["cover"] = body.artworkUrl100

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
                }

                override fun onFailure(call: Call<Tracks>, t: Throwable) {

//                    playIcon.setImageResource(R.drawable.icon_play_disabled)
//                    favIcon.setImageResource(R.drawable.icon_fav_empty)
//                    mediaIcon.setImageResource(R.drawable.icon_media_empty)
//
//                    Toast.makeText(
//                        this@PlayerActivity,
//                        "Ошибка: не удалось связаться с сервером",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })

        return mapOfSpecs
    }


}