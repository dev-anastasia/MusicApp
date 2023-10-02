package com.example.musicapp

import com.example.musicapp.network.MusicService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {

    val musicService: MusicService = Retrofit.Builder()
        .baseUrl(SearchActivity.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(interceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MusicService::class.java)
}

