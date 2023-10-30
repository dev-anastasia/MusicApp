package com.example.musicapp.data.network

import com.example.musicapp.presentation.ui.search.SearchFragment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {

    val musicService: MusicService = Retrofit.Builder()
        .baseUrl(SearchFragment.BASE_URL)
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

