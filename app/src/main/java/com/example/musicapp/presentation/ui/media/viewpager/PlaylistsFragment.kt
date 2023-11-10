package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Creator
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.media.adapter.MediaPlaylistsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistsFragment(override var currId: Int = 0) :
    Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: MediaPlaylistsAdapter
    private lateinit var vm: PlaylistsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: PlaylistsViewModel =
            ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        mediaAdapter = MediaPlaylistsAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm.getList(requireContext())

        vm.allPlaylists.observe(viewLifecycleOwner) { list ->
            mediaAdapter.updateList(list)   // notify'и здесь или в адаптере не помогают
        }

        // Кнопка добавления плейлиста
        view.findViewById<FloatingActionButton>(R.id.fab_add_playlist).setOnClickListener {
            activity?.supportFragmentManager!!.beginTransaction()
                .add(R.id.media_container_main, AddPlaylistFragment())
                .addToBackStack("AddPlaylistFragment")
                .setReorderingAllowed(true)
                .commit()
        }
    }

    override fun openPlaylistClicked(id: Int) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val fr = SinglePlaylistFragment()
        val bundle = Bundle()
        bundle.putInt(ID_KEY, id)
        fr.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .add(R.id.media_container_main, fr)
            .addToBackStack("SinglePlaylistFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    override fun deletePlaylistClicked() {
        vm.deletePlaylist(requireContext(), currId)
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}