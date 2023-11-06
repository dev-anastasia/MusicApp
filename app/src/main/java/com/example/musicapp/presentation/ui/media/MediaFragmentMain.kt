package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.ui.media.viewpager.PlaylistsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// Содержит ViewPager

class MediaFragmentMain : Fragment(R.layout.fragment_media_main), OnPlaylistClickListener {

    private lateinit var adapter: PlaylistsPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ниже - отдельный адаптер для ViewPager'а!
        adapter = PlaylistsPagerAdapter(this)
        viewPager = view.findViewById(R.id.fragment_media_pager)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.fragment_media_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
        }.attach()
    }

    override fun onPlaylistClick(id: Int) {
        val playlistFragment = SinglePlaylistFragment()
        val bundle = Bundle()
        bundle.putInt(ID_KEY, id)
        playlistFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.search_container, playlistFragment)
            .addToBackStack("added PlaylistFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}