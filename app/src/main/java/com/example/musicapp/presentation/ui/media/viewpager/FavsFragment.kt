package com.example.musicapp.presentation.ui.media.viewpager

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.ui.media.tracksAdapter.TrackEntityAdapter
import com.example.musicapp.presentation.ui.player.PlayerFragment

class FavsFragment : Fragment(R.layout.favs_fragment), OnTrackClickListener {

    private lateinit var favsAdapter: TrackEntityAdapter
    private val vm: TracksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView =
            view.findViewById<RecyclerView>(R.id.favs_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        favsAdapter = TrackEntityAdapter(this)
        recyclerView.adapter = favsAdapter

        vm.getTracksIdsList(requireActivity().applicationContext, 0)

        vm.allTracks.observe(viewLifecycleOwner) { list ->
            favsAdapter.updateList(list)
        }

        if (vm.allTracks.value!!.isEmpty()) {     // Если плейлист избранных треков пустой
            view.findViewById<LinearLayout>(R.id.ll_favs_playlist_fragment_empty_playlist)
                .visibility = View.VISIBLE
        }
    }

    override fun onItemClick(id: Long) {
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        playerFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.media_container_main, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    companion object {
        const val TRACK_ID = "id key"
    }
}