package com.example.musicapp.domain.player

import com.example.musicapp.Creator.mediaPlayer
import com.example.musicapp.presentation.PlayerClass
import java.util.concurrent.Executors

class PlayerClassImpl : PlayerClass {

    override fun setPlayer(dataSource: String) {

        mediaPlayer.apply {
            if (this.isPlaying.not()) {
                setDataSource(dataSource)
                prepareAsync()
            }
        }
    }

    override fun playPlayer(callback: () -> Unit) {

        Executors.newSingleThreadExecutor().execute { // Решила вынести проигрывание из ui-потока

            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {   // При завершении трека:
                stopPlayer()
                callback()
            }
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun stopPlayer() {
        mediaPlayer.stop()
    }
}