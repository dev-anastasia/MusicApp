package com.example.musicapp.presentation.ui.media.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment

class PlaylistsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            (0) -> PlaylistsFragment()
            else -> SinglePlaylistFragment()
        }
    }
}