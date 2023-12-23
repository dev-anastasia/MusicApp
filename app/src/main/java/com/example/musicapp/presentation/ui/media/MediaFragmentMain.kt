package com.example.musicapp.presentation.ui.media

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import com.example.musicapp.presentation.presenters.factories.PlaylistsVMFactory
import com.example.musicapp.presentation.ui.media.viewpager.AddPlaylistFragment
import com.example.musicapp.presentation.ui.media.viewpager.viewPagerAdapter.PlaylistsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

// Содержит ViewPager

class MediaFragmentMain : Fragment(R.layout.fragment_media_main) {

    @Inject
    lateinit var vmFactory: PlaylistsVMFactory
    private val vm: PlaylistsViewModel by activityViewModels { vmFactory } // Владелец - MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mediaSubcomponent =
            requireActivity().applicationContext.component.mediaSubcomponent().create()
        mediaSubcomponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ниже - отдельный адаптер для ViewPager'а!
        val viewPager = view.findViewById<ViewPager2>(R.id.fragment_media_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.fragment_media_tab_layout)

        viewPager.adapter = PlaylistsPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Плейлисты"
            } else {
                tab.text = "Избранное"
            }
        }.attach() // так мы соединяем tabLayout и viewPager
    }

    override fun onResume() {
        requireView().findViewById<ImageButton>(R.id.media_main_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }

        requireView().findViewById<Toolbar>(R.id.media_fragment_toolbar).setOnClickListener {
            if (vm.addPlaylistFragmentIsOpen.value!!.not()) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, AddPlaylistFragment())
                    .addToBackStack("AddPlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }
        }
        super.onResume()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}