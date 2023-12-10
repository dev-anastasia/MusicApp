package com.example.musicapp.presentation.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.MyApp
import com.example.musicapp.R
import com.example.musicapp.presentation.ui.media.MediaFragmentMain
import com.example.musicapp.presentation.ui.search.SearchFragment
import com.example.musicapp.presentation.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = MyApp()
        app.initDatabase()

        findViewById<FrameLayout>(R.id.activity_main_fl_search).setOnClickListener {
            val searchFr = SearchFragment()
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, searchFr)
                    .addToBackStack("added SearchFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }

        findViewById<FrameLayout>(R.id.activity_main_fl_media).setOnClickListener {
            val mediaFr = MediaFragmentMain()
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, mediaFr)
                    .addToBackStack("added MediaFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }

        findViewById<FrameLayout>(R.id.activity_main_fl_settings).setOnClickListener {
            val settingsFr = SettingsFragment()
            if (savedInstanceState == null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, settingsFr)
                    .addToBackStack("added SettingsFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }
    }
}