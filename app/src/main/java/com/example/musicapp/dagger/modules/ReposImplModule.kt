package com.example.musicapp.dagger.modules

import com.example.musicapp.data.repos.PlaylistsRepoImpl
import com.example.musicapp.data.repos.TracksRepoImpl
import dagger.Module

@Module
interface ReposImplModule {

    fun inject(repo: PlaylistsRepoImpl)

    fun inject(repo: TracksRepoImpl)
}