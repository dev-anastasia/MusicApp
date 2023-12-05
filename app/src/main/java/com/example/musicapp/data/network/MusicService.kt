package com.example.musicapp.data.network

import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.TracksList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {
    @GET("search")
    fun getSearchResult(@Query("term") keyword: String, @Query("entity") entity: String): Single<Music>

    @GET("lookup")
    fun getTrackInfoById(@Query("id") id: Long) : Single<TracksList>
}