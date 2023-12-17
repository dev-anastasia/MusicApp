package com.example.musicapp.presentation

interface PlayerClass {

    fun setPlayer(dataSource: String)

    fun playPlayer(callback: () -> Unit)

    fun pausePlayer()

    fun stopPlayer()
}