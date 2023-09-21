package com.example.musicapp.network

import com.example.musicapp.data.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {
    @GET("search?")
    fun getSearchResult(@Query("term") keyword: String, @Query("entity") entity: String): Call<Music>
}