package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.domain.database.PlaylistEntity
import com.example.musicapp.presentation.presenters.PlaylistsViewModel

class AddPlaylistFragment : Fragment(R.layout.fragment_add_playlist) {

    private lateinit var vm: PlaylistsViewModel  // владелец - MediaActivity
    private lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]

        editText = view.findViewById(R.id.et_new_playlist_name)

        val commitBtn: ImageButton = view.findViewById(R.id.btn_commit_add_playlist)
        val cancelBtn: ImageButton = view.findViewById(R.id.btn_cancel_adding)

        // Открыть клавиатуру
        editText.requestFocus()
        val v: View? = activity?.currentFocus
        val inputMethodManager = activity?.getSystemService(InputMethodManager::class.java)
        inputMethodManager?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)

        // Обработка нажатия Enter на клавиатуре
        editText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_UP) {
                    addPlaylist()
                    return@setOnKeyListener true
                }
            }
            false
        }

        cancelBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        commitBtn.setOnClickListener {
            addPlaylist()
        }
    }

    private fun addPlaylist() {

        val playlistName = (
                if (editText.text.isEmpty().not())
                    editText.text.toString()
                else
                    "My Playlist"
                )

        val newPlaylist = PlaylistEntity(
            0,
            null,
            playlistName,
            0,
            System.currentTimeMillis()
        )
        vm.addPlaylist(requireActivity().applicationContext, newPlaylist)
        requireActivity().supportFragmentManager.popBackStack()
    }
}