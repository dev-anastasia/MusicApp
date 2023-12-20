package com.example.musicapp.presentation.ui.player

import com.example.musicapp.MyObject.mediaPlayer
import javax.inject.Inject

class PlayerClassImpl @Inject constructor() : PlayerClass {

    override fun setPlayer(dataSource: String, callback: () -> Unit) {
        try {
            mediaPlayer.apply {
                setDataSource(dataSource)
                prepareAsync()
                setOnPreparedListener {
                    callback()
                }
            }
        } catch (e: Exception) {
            throw java.lang.Exception("Empty MediaPlayer datasource(?): $e")
        }
    }

    override fun playPlayer(callback: () -> Unit) {
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {   // При завершении трека:
            callback()
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun stopPlayer() {
        mediaPlayer.stop()
    }
}