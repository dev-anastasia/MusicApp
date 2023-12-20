package com.example.musicapp.presentation.ui.player

interface PlayerClass {

    fun setPlayer(dataSource: String, callback: () -> Unit)

    fun playPlayer()

    fun pausePlayer()

    fun stopAndReleasePlayer()

    fun countCurrentTime(): String

    fun countDuration(): String
}