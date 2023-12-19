package com.example.musicapp.dagger

import com.example.musicapp.domain.useCases.playlists.DeletePlaylistUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistInfoUseCase
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import com.example.musicapp.domain.useCases.playlists.InsertPlaylistUseCase
import com.example.musicapp.domain.useCases.tracks.DeleteTrackUseCase
import com.example.musicapp.domain.useCases.tracks.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.domain.useCases.tracks.InsertTrackUseCase
import com.example.musicapp.presentation.presenters.factories.PlayerVMFactory
import com.example.musicapp.presentation.presenters.factories.PlaylistsVMFactory
import com.example.musicapp.presentation.presenters.factories.SearchVMFactory
import com.example.musicapp.presentation.presenters.factories.TracksVMFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun providesSearchVMFactory(getTracksListUseCase: GetTracksListUseCase): SearchVMFactory {
        return SearchVMFactory(getTracksListUseCase)
    }

    @Provides
    fun providesPlaylistsVMFactory(
        getPlaylistsUseCase: GetPlaylistsUseCase,
        insertPlaylistUseCase: InsertPlaylistUseCase,
        deletePlaylistUseCase: DeletePlaylistUseCase,
        getPlaylistInfoUseCase: GetPlaylistInfoUseCase
    ): PlaylistsVMFactory {
        return PlaylistsVMFactory(
            getPlaylistsUseCase,
            insertPlaylistUseCase,
            deletePlaylistUseCase,
            getPlaylistInfoUseCase
        )
    }

    @Provides
    fun providesPlayerVMFactory(
        getPlaylistsUseCase: GetPlaylistsUseCase,
        getTracksListUseCase: GetTracksListUseCase,
        insertTrackUseCase: InsertTrackUseCase,
        deleteTrackUseCase: DeleteTrackUseCase,
        getTrackInfoUseCase: GetTrackInfoUseCase
    ): PlayerVMFactory {
        return PlayerVMFactory(
            getPlaylistsUseCase,
            getTracksListUseCase,
            insertTrackUseCase,
            deleteTrackUseCase,
            getTrackInfoUseCase
        )
    }

    @Provides
    fun providesTracksVMFactory(getTracksListUseCase: GetTracksListUseCase): TracksVMFactory {
        return TracksVMFactory(getTracksListUseCase)
    }
}