package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.R
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.presenters.PlaylistViewModel

class AddPlaylistFragment : Fragment(R.layout.fragment_add_playlist) {

    private lateinit var vm: PlaylistViewModel  // владелец - MediaActivity
    private lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]
        vm.changeAddPlaylistFragmentIsOpen(true)

        editText = view.findViewById(R.id.et_new_playlist_name)

        val commitBtn: ImageButton = view.findViewById(R.id.btn_add_playlist)
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

    override fun onDestroy() {
        vm.changeAddPlaylistFragmentIsOpen(false)
        super.onDestroy()
    }

    private fun addPlaylist() {

        val playlistName =
            if (editText.text.isEmpty()) {
                R.string.new_playlist_default_name.toString()
            } else {
                editText.text.toString()
            }
        val newPlaylist = Playlist(
            0,
            playlistName,
            null,
            System.currentTimeMillis()
        )
        vm.addPlaylist(newPlaylist)
        requireActivity().supportFragmentManager.popBackStack()
    }
}