package com.example.musicapp.dagger

import com.example.musicapp.data.repos.TracksRepoImpl
import com.example.musicapp.domain.TracksRepo
import dagger.Binds
import dagger.Module

@Module
interface TracksModule {

    @Binds
    fun providesTracksRepo(impl: TracksRepoImpl): TracksRepo
}