package com.example.musicapp.dagger.components

import dagger.Module

@Module(subcomponents = [MediaSubcomponent::class, SearchSubcomponent::class])
class AppSubcomponents