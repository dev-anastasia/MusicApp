package com.example.musicapp.presentation.ui.player

interface PlayerClass {

    fun setPlayer(dataSource: String, callback: () -> Unit)

    fun playPlayer(callback: () -> Unit)

    fun pausePlayer()

    fun stopPlayer()
}