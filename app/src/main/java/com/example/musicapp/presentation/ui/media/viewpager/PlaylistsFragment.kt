package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.example.musicapp.presentation.presenters.MediaViewModel
import com.example.musicapp.presentation.ui.media.AddPlaylistFragment
import com.example.musicapp.presentation.ui.media.SinglePlaylistFragment
import com.example.musicapp.presentation.ui.media.adapter.MediaPlaylistsAdapter

class PlaylistsFragment : Fragment(R.layout.fragment_playlists),
    OnPlaylistClickListener {

    private lateinit var mediaAdapter: MediaPlaylistsAdapter
    private val vm: MediaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.media_fragment_playlists_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер
        mediaAdapter = MediaPlaylistsAdapter(this)
        recyclerView.adapter = mediaAdapter

        vm.initList() // Создает пустой список, если в нем null
        vm.newList.observe(viewLifecycleOwner) { list ->
            mediaAdapter.updateList(list)
        }
    }

    override fun onPlaylistClick(id: Int) {

        when (id) {
            (-2) -> {
                val fr = AddPlaylistFragment()
                activity?.supportFragmentManager!!.beginTransaction()
                    .add(R.id.media_container_main, fr)
                    .addToBackStack("AddPlaylistFragment")
                    .setReorderingAllowed(true)
                    .commit()
            }

            else -> {
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
        }
    }

    private companion object {
        const val ID_KEY = "id key"
    }
}