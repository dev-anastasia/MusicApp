package com.example.musicapp.dagger

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.domain.PlaylistsRepo
import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistInfoUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.presenters.PlaylistViewModel
import com.example.musicapp.presentation.presenters.SearchViewModel
import dagger.Module
import dagger.Provides

@Module
object PlaylistsModule {

    @Provides
    fun providesPlaylistsRepo(): PlaylistsRepo {
        return PlaylistsRepoImpl()
    }

    @Provides
    fun providesGetPlaylistsUseCase(repo: PlaylistsRepo): GetPlaylistsUseCase {
        return GetPlaylistsUseCase(repo)
    }

    @Provides
    fun providesInsertPlaylistUseCase(repo: PlaylistsRepo): InsertPlaylistUseCase {
        return InsertPlaylistUseCase(repo)
    }

    @Provides
    fun providesDeletePlaylistUseCase(repo: PlaylistsRepo): DeletePlaylistUseCase {
        return DeletePlaylistUseCase(repo)
    }

    @Provides
    fun providesGetPlaylistInfoUseCase(repo: PlaylistsRepo): GetPlaylistInfoUseCase {
        return GetPlaylistInfoUseCase(repo)
    }
}