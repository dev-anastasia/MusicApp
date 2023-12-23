package com.example.musicapp.presentation.ui.player

import android.media.MediaPlayer
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

    override fun playPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun stopAndReleasePlayer() {
        mediaPlayer.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer()
    }

    override fun countCurrentTime(): String {
        val currTimeInMinutes = mediaPlayer.currentPosition / 1000 / 60
        var currTimeInSeconds = (mediaPlayer.currentPosition / 1000 % 60).toString()
        if (currTimeInSeconds.length < 2) {
            currTimeInSeconds = "0$currTimeInSeconds"     // вместо "1:7" -> "1:07"
        }
        return "$currTimeInMinutes:$currTimeInSeconds"
    }

    override fun countDuration(): String {
        val millis = mediaPlayer.duration
        if (millis == -1) {
            return DURATION_DEFAULT
        }

        val mins = millis / 1000 / 60
        val currSecs = millis / 1000 % 60
        var secs = currSecs.toString()
        if (currSecs.toString().length < 2) {
            secs = "0$secs"
        }
        return "$mins:$secs"
    }

    private companion object {
        const val DURATION_DEFAULT = "0:00"
    }
}