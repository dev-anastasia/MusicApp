package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.musicapp.R

class FavsFragment : Fragment(R.layout.favs_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = this@FavsFragment.arguments?.getInt(ID_KEY)
        if (id == null) {
            view.findViewById<LinearLayout>(R.id.ll_favs_playlist_fragment_empty_playlist)
                .visibility = View.VISIBLE
        } else {

        }
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}