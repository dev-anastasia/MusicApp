package com.example.musicapp.dagger.modules

import com.example.musicapp.dagger.components.MediaSubcomponent
import com.example.musicapp.dagger.components.SearchSubcomponent
import dagger.Module

@Module(subcomponents = [MediaSubcomponent::class, SearchSubcomponent::class])
class AppSubcomponents