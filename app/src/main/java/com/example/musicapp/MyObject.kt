package com.example.musicapp

import android.content.Context
import android.media.MediaPlayer
import com.example.musicapp.domain.database.MyDao
import com.example.musicapp.domain.database.PlaylistDatabase

object MyObject {

    const val FAVS_PLAYLIST_ID = -1

    var dao: MyDao? = null  // Объект создаётся при входе в приложение, в MainActivity

    var mediaPlayer = MediaPlayer()
}