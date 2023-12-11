package com.example.musicapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.MyApp
import com.example.musicapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        val app = MyApp()
        val apContext = applicationContext
        app.initDatabase(apContext)

        val mainScreenFragment = MainScreenFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, mainScreenFragment)
            .setReorderingAllowed(true)
            .commit()
    }
}