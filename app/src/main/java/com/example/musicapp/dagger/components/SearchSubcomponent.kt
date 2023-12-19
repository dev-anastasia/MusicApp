package com.example.musicapp.dagger.components

import com.example.musicapp.presentation.ui.search.SearchFragment
import dagger.Subcomponent

@Subcomponent
interface SearchSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchSubcomponent
    }

    fun inject(fr: SearchFragment)
}