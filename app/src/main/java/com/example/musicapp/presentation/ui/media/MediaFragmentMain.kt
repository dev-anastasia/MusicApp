package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.presentation.ui.media.viewpager.PlaylistsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// Содержит ViewPager

class MediaFragmentMain : Fragment(R.layout.fragment_media_main) {

    private lateinit var adapter: PlaylistsPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ниже - отдельный адаптер для ViewPager'а!
        adapter = PlaylistsPagerAdapter(this)
        viewPager = view.findViewById(R.id.fragment_media_pager)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.fragment_media_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0)
                tab.text = "Плейлисты"
            else
                tab.text = "Избранное"
        }.attach() // так мы соединяем tabLayout и viewPager

        val btnGoBack = view.findViewById<ImageButton>(R.id.media_main_fragment_btn_go_back)

        btnGoBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}