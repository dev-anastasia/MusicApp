package com.example.musicapp.presentation.ui.media.viewpager.viewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.presentation.ui.media.viewpager.FavsFragment
import com.example.musicapp.presentation.ui.media.viewpager.MediaPlaylistListFragment

class PlaylistsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            (0) -> MediaPlaylistListFragment()
            else -> FavsFragment()
        }
    }
}