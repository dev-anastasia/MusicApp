package com.example.musicapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.application.MainApp
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.domain.useCases.playlists.GetPlaylistsUseCase
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        val app = MainApp()
        app.initDatabase(applicationContext)

        val mainScreenFragment = MainScreenFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, mainScreenFragment)
            .setReorderingAllowed(true)
            .commit()
    }
}