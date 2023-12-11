package com.example.musicapp.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.musicapp.R
import com.example.musicapp.presentation.ui.media.MediaFragmentMain
import com.example.musicapp.presentation.ui.search.SearchFragment
import com.example.musicapp.presentation.ui.settings.SettingsFragment

class MainScreenFragment: Fragment(R.layout.fragment_main_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FrameLayout>(R.id.activity_main_fl_search).setOnClickListener {
            val searchFr = SearchFragment()
            if (savedInstanceState == null) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, searchFr)
                    .addToBackStack("added SearchFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }

        view.findViewById<FrameLayout>(R.id.activity_main_fl_media).setOnClickListener {
            val mediaFr = MediaFragmentMain()
            if (savedInstanceState == null) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, mediaFr)
                    .addToBackStack("added MediaFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }

        view.findViewById<FrameLayout>(R.id.activity_main_fl_settings).setOnClickListener {
            val settingsFr = SettingsFragment()
            if (savedInstanceState == null) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, settingsFr)
                    .addToBackStack("added SettingsFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }
    }
}