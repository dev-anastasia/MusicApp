package com.example.musicapp.dagger.components

import com.example.musicapp.dagger.scopes.MediaScope
import com.example.musicapp.presentation.ui.media.MediaFragmentMain
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.AddPlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.FavsFragment
import com.example.musicapp.presentation.ui.media.viewpager.MediaPlaylistListFragment
import dagger.Subcomponent

@MediaScope
@Subcomponent
interface MediaSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MediaSubcomponent
    }

    fun inject(fr: MediaFragmentMain)

    fun inject(fr: AddPlaylistFragment)

    fun inject(fr: MediaPlaylistListFragment)

    fun inject(fr: SinglePlaylistFragment)

    fun inject(fr: FavsFragment)
}