package com.example.musicapp.presentation.ui.media.viewpager

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.domain.entities.PlaylistInfo
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.PlaylistsViewModel
import com.example.musicapp.presentation.presenters.factories.PlaylistsVMFactory
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.playlistAdapter.PlaylistAdapter
import javax.inject.Inject

class MediaPlaylistListFragment :
    Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    @Inject
    lateinit var vmFactory: PlaylistsVMFactory
    private val vm: PlaylistsViewModel by activityViewModels { vmFactory } // владелец - MediaActivity

    override fun onAttach(context: Context) {
        val mediaSubcomponent =
            requireActivity().applicationContext.component.mediaSubcomponent().create()
        mediaSubcomponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingIcon: ImageView = view.findViewById(R.id.playlists_list_fragment_iv_loading)
        val message: LinearLayout = view.findViewById(R.id.ll_playlists_list_is_empty_message)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        val mediaAdapter = PlaylistAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm.apply {

            getListOfUsersPlaylists()

            allPlaylists.observe(viewLifecycleOwner) { list ->
                mediaAdapter.updateList(list)
                recyclerView.scrollToPosition(0)
            }

            playlistsListUiState.observe(viewLifecycleOwner) {

                when (it) {

                    PlaylistsListUiState.Loading -> {
                        loadingIcon.visibility = View.VISIBLE
                        message.visibility = View.GONE
                    }

                    PlaylistsListUiState.Success -> {
                        loadingIcon.visibility = View.GONE
                        message.visibility = View.GONE
                    }

                    PlaylistsListUiState.NoResults -> {
                        loadingIcon.visibility = View.GONE
                        message.visibility = View.VISIBLE
                    }
                }
            }
        }
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
        vm.getPlaylistInfo(playlistId) {
            callback(it)
        }
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}