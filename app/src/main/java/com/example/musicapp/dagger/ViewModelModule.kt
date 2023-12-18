package com.example.musicapp.dagger

import com.example.musicapp.domain.useCases.tracks.GetTracksListUseCase
import com.example.musicapp.presentation.presenters.factories.SearchVMFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun providesFactory(getTracksListUseCase: GetTracksListUseCase): SearchVMFactory {
        return SearchVMFactory(getTracksListUseCase)
    }
}