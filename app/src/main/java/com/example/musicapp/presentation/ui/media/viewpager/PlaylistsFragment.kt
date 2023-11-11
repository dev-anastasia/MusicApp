package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.media.playlistsAdapter.MediaPlaylistsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistsFragment() :
    Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: MediaPlaylistsAdapter
    private lateinit var vm: PlaylistsViewModel  // владелец - MediaActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        vm = ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]

        vm.getList(requireContext())

        vm.allPlaylists.observe(viewLifecycleOwner) { list ->
            mediaAdapter.updateList(list)
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

    override fun deletePlaylistClicked(id: Int) {
        vm.deletePlaylist(requireContext(), id)
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}