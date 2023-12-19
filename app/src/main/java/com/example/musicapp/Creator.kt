package com.example.musicapp

import android.media.MediaPlayer
import com.example.musicapp.domain.database.MyDao
import com.example.musicapp.domain.player.PlayerClassImpl
import com.example.musicapp.presentation.PlayerClass

object Creator {

    var dao: MyDao? = null  // Объект создаётся при входе в приложение, в MainActivity

    val playerClass: PlayerClass = PlayerClassImpl()

    var mediaPlayer = MediaPlayer()
}