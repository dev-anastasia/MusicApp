package com.example.musicapp.presentation.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchFragment = SearchFragment()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.search_container, searchFragment)
                .setReorderingAllowed(true)
                .commit()
        }
    }
}