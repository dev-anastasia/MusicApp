package com.example.musicapp

import com.example.musicapp.data.repos.SearchRepoImpl
import com.example.musicapp.data.repos.TrackInfoRepoImpl
import com.example.musicapp.domain.SearchRepo
import com.example.musicapp.domain.TrackInfoRepo
import com.example.musicapp.domain.useCases.GetTrackInfoUseCase
import com.example.musicapp.domain.useCases.GetTrackListUseCase
import com.example.musicapp.presentation.presenters.SearchViewModel


object Creator {

    private val searchRepo: SearchRepo = SearchRepoImpl() // Приводим к типу интерфейса
    val searchUseCase = GetTrackListUseCase(searchRepo)

    private val trackInfoRepo: TrackInfoRepo = TrackInfoRepoImpl()
    val trackInfoUseCase = GetTrackInfoUseCase(trackInfoRepo)


    fun updateUseCase(vm: SearchViewModel){
        searchUseCase.setVM(vm)
    }
}