package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.musicapp.R

class AddPlaylistFragment : Fragment(R.layout.fragment_add_playlist) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commitBtn: ImageButton = view.findViewById(R.id.btn_commit_add_playlist)
        val cancelBtn: ImageButton = view.findViewById(R.id.btn_cancel_adding)

        commitBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        cancelBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}