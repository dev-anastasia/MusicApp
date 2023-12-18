package com.example.musicapp.dagger

import com.example.musicapp.data.repos.TracksRepoImpl
import com.example.musicapp.domain.TracksRepo
import com.example.musicapp.domain.player.PlayerClassImpl
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.PlayerClass
import dagger.Module
import dagger.Provides

@Module
object TracksModule {

    @Provides
    fun providesTracksRepo(): TracksRepo {
        return TracksRepoImpl()
    }

    @Provides
    fun providesGetTracksListUseCase(repo: TracksRepo): GetTracksListUseCase {
        return GetTracksListUseCase(repo)
    }

    @Provides
    fun providesInsertTrackUseCase(repo: TracksRepo): InsertTrackUseCase {
        return InsertTrackUseCase(repo)
    }

    @Provides
    fun providesDeleteTrackUseCase(repo: TracksRepo): DeleteTrackUseCase {
        return DeleteTrackUseCase(repo)
    }

    @Provides
    fun providesGetTrackInfoUseCase(repo: TracksRepo): GetTrackInfoUseCase {
        return GetTrackInfoUseCase(repo)
    }

    @Provides
    fun providesPlayerClass(): PlayerClass {
        return PlayerClassImpl()
    }
}