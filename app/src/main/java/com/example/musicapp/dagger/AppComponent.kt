package com.example.musicapp.dagger

import com.example.musicapp.presentation.ui.media.MediaFragmentMain
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.AddPlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.FavsFragment
import com.example.musicapp.presentation.ui.media.viewpager.MediaPlaylistListFragment
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.search.SearchFragment
import dagger.Component

@Component(
    modules = [
        PlaylistsModule::class,
        TracksModule::class,
        PlayerModule::class]
)
interface AppComponent {

    fun inject(fr: SearchFragment)

    fun inject(fr: MediaFragmentMain)

    fun inject(fr: AddPlaylistFragment)

    fun inject(fr: MediaPlaylistListFragment)

    fun inject(fr: FavsFragment)

    fun inject(fr: SinglePlaylistFragment)

    fun inject(fr: PlayerFragment)
}