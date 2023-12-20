package com.example.musicapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.MyObject
import com.example.musicapp.R
import com.example.musicapp.application.MainApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        val app = MainApp()
        app.initDatabase(applicationContext)

        val mainScreenFragment = MainScreenFragment()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, mainScreenFragment)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}