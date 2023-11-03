package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val mediaFragment = MediaFragment()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.media_container, mediaFragment)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}