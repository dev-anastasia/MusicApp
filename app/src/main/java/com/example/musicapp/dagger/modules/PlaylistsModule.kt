package com.example.musicapp.dagger.modules

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.domain.PlaylistsRepo
import dagger.Binds
import dagger.Module

@Module
interface PlaylistsModule {

    @Binds
    fun providesPlaylistsRepo(impl: PlaylistsRepoImpl): PlaylistsRepo
}