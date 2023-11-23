package com.example.musicapp.presentation.ui.media

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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

class SinglePlaylistFragment : Fragment(R.layout.single_playlist_fragment),
    OnTrackClickListener {

    private lateinit var tracksAdapter: TrackEntityAdapter
    private val vm: TracksViewModel by viewModels()
    private val playlistId: Int
        get() {
            if (_playlistId == null)
                _playlistId = this@SinglePlaylistFragment.arguments?.getInt(ID_KEY)
            return _playlistId!!
        }
    private var _playlistId: Int? = null    // packing property

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyPlaylistMessage: LinearLayout =
            view.findViewById(R.id.ll_single_playlist_fragment_empty_playlist)

        // Адаптер - ДОЛЖЕН использоваться тот же, что в поисковике (маппинг TrackEntity в Track!)
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.single_playlist_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel
        tracksAdapter = TrackEntityAdapter(this)
        recyclerView.adapter = tracksAdapter

        vm.apply {

            getTracksList(requireActivity().applicationContext, playlistId)

            tracksList.observe(viewLifecycleOwner) {
                tracksAdapter.updateList(it)

                if (it!!.isEmpty())
                    emptyPlaylistMessage.visibility = View.VISIBLE
                else
                    emptyPlaylistMessage.visibility = View.GONE
            }
        }
    }

    override fun onResume() {

        requireView().findViewById<ImageButton>(R.id.single_playlist_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }

        super.onResume()
    }

    override fun onItemClick(id: Long) {
        // Открытие PlayerFragment'а
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

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private companion object {
        const val ID_KEY = "id key"
        const val TRACK_ID = "track id key"
    }
}