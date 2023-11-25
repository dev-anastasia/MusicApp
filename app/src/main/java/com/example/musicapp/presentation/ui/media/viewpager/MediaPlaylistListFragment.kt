package com.example.musicapp.presentation.ui.media.viewpager

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.PlaylistViewModel
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.playlistAdapter.PlaylistAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MediaPlaylistListFragment :
    Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: PlaylistAdapter
    private lateinit var vm: PlaylistViewModel  // владелец - MediaActivity
    private val apContext: Context
        get() {
            return requireActivity().applicationContext
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addFrBtn = view.findViewById<FloatingActionButton>(R.id.fab_add_playlist)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        mediaAdapter = PlaylistAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]

        vm.apply {

            allPlaylists.observe(viewLifecycleOwner) { list ->
                mediaAdapter.updateList(list)
            }

            addPlaylistFragmentIsOpen.observe(viewLifecycleOwner) {
                if (it == true)
                    addFrBtn.visibility = View.GONE
                else
                    addFrBtn.visibility = View.VISIBLE
            }
        }

        // Кнопка добавления плейлиста
        addFrBtn.setOnClickListener {
            if (vm.addPlaylistFragmentIsOpen.value!!.not())
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.media_container_main, AddPlaylistFragment())
                    .addToBackStack("AddPlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
        }
    }

    override fun onResume() {
        vm.getListOfUsersPlaylists(apContext)

        super.onResume()
    }

    override fun openPlaylistClicked(id: Int) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val fr = SinglePlaylistFragment()
        val bundle = Bundle()
        bundle.putInt(ID_KEY, id)
        fr.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.media_container_main, fr)
            .addToBackStack("SinglePlaylistFragment")
            .setReorderingAllowed(true)
            .commit()
    }


    override fun deletePlaylistClicked(id: Int) {
        vm.deletePlaylist(apContext, id)
    }

    override fun getPlaylistTracksCount(playlistId: Int, callback: (Int) -> Unit) {
        vm.getPlaylistTracksCount(playlistId, apContext) {
            callback(it)
        }
    }

    override fun getPlaylistCover(playlistId: Int, callback: (String?) -> Unit) {
        vm.getPlaylistCover(playlistId, apContext) {
            callback(it)
        }
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}