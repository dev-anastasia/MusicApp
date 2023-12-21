package com.example.musicapp.dagger.components

import com.example.musicapp.application.MainApp
import com.example.musicapp.dagger.modules.AppModule
import com.example.musicapp.dagger.modules.DatabaseModule
import com.example.musicapp.dagger.modules.PlayerModule
import com.example.musicapp.dagger.modules.PlaylistsModule
import com.example.musicapp.dagger.modules.ReposImplModule
import com.example.musicapp.dagger.modules.TracksModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        PlaylistsModule::class,
        TracksModule::class,
        PlayerModule::class,
        ReposImplModule::class,
        DatabaseModule::class,
        AppModule::class,
        AppSubcomponents::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(app: MainApp): Builder

        fun build(): AppComponent
    }

    fun mediaSubcomponent(): MediaSubcomponent.Factory

    fun searchSubcomponent(): SearchSubcomponent.Factory

    fun playerSubcomponent(): PlayerSubcomponent.Factory
}