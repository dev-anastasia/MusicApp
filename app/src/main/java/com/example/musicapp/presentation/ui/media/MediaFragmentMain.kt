package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.presentation.presenters.PlaylistViewModel
import com.example.musicapp.presentation.ui.media.viewpager.AddPlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.viewPagerAdapter.PlaylistsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

// Содержит ViewPager

class MediaFragmentMain : Fragment(R.layout.fragment_media_main) {

    private lateinit var adapter: PlaylistsPagerAdapter
    private lateinit var viewPager: ViewPager2
    @Inject
    lateinit var vm: PlaylistViewModel  // владелец - MediaActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        vm = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
        val toolbar = view.findViewById<Toolbar>(R.id.media_fragment_toolbar)

        // ниже - отдельный адаптер для ViewPager'а!
        adapter = PlaylistsPagerAdapter(this)
        viewPager = view.findViewById(R.id.fragment_media_pager)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.fragment_media_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Плейлисты"
            } else {
                tab.text = "Избранное"
            }
        }.attach() // так мы соединяем tabLayout и viewPager

        // Кнопка добавления плейлиста
        toolbar.setOnClickListener {
            if (vm.addPlaylistFragmentIsOpen.value!!.not())
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, AddPlaylistFragment())
                    .addToBackStack("AddPlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
        }

        view.findViewById<ImageButton>(R.id.media_main_fragment_btn_go_back).setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}