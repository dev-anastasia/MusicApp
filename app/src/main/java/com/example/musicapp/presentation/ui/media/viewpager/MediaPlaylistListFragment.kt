package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.domain.entities.PlaylistInfo
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.factories.PlaylistsVMFactory
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.playlistAdapter.PlaylistAdapter
import javax.inject.Inject

class MediaPlaylistListFragment :
    Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: PlaylistAdapter
    @Inject
    lateinit var vmFactory: PlaylistsVMFactory
    private lateinit var vm: PlaylistsViewModel  // владелец - MediaActivity
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        requireActivity().applicationContext.component.inject(this)
        vm = ViewModelProvider(requireActivity(), vmFactory)[PlaylistsViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView =
            view.findViewById(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        mediaAdapter = PlaylistAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm = ViewModelProvider(requireActivity())[PlaylistsViewModel::class.java]

        vm.allPlaylists.observe(viewLifecycleOwner) { list ->
            mediaAdapter.updateList(list)
        }
    }

    override fun onResume() {
        vm.getListOfUsersPlaylists()
        super.onResume()
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

    override fun openPlaylistClicked(id: Int) {
        // Открытие фрагмента со списком треков плейлиста, id передаётся адаптером
        val fr = SinglePlaylistFragment()
        val bundle = Bundle()
        bundle.putInt(ID_KEY, id)
        fr.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_container, fr)
            .addToBackStack("SinglePlaylistFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    override fun deletePlaylistClicked(id: Int) {
        vm.deletePlaylist(id)
    }

    override fun getPlaylistInfo(
        playlistId: Int,
        callback: (PlaylistInfo) -> Unit
    ) {
        vm.getPlaylistInfo(playlistId, callback)
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}