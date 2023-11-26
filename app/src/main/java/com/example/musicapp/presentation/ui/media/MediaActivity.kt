package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.presentation.presenters.PlaylistViewModel

class MediaActivity : AppCompatActivity() {

    val vm: PlaylistViewModel by viewModels() // Используется фрагментами!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val mediaFr = MediaFragmentMain()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.media_container_main, mediaFr)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}