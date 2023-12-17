package com.example.musicapp.dagger

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.domain.PlaylistsRepo
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun providesPlaylistsRepo(): PlaylistsRepo {
        return PlaylistsRepoImpl()
    }
}