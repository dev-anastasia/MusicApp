package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.musicapp.R
import com.example.musicapp.domain.entities.room.PlaylistDBObject
import com.example.musicapp.presentation.presenters.PlaylistsViewModel

class AddPlaylistFragment : Fragment(R.layout.fragment_add_playlist) {

    private val vm: PlaylistsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commitBtn: ImageButton = view.findViewById(R.id.btn_commit_add_playlist)
        val cancelBtn: ImageButton = view.findViewById(R.id.btn_cancel_adding)
        val editText: EditText = view.findViewById(R.id.et_new_playlist_name)

        cancelBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        fun addPlaylist() {

            val playlistName = (
                    if (editText.text.isEmpty().not())
                        editText.text.toString()
                    else
                        "My Playlist"
                    )

            val newPlaylist = PlaylistDBObject(
                0,
                null,
                playlistName,
                0,
                System.currentTimeMillis()
            )
            vm.addPlaylist(newPlaylist)
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Inserted!", Toast.LENGTH_SHORT).show()
        }


        commitBtn.setOnClickListener {
            addPlaylist()
        }
    }
}