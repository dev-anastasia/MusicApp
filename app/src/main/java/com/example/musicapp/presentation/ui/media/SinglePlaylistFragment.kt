package com.example.musicapp.presentation.ui.media

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.application.component
import com.example.musicapp.presentation.OnTrackClickListener
import com.example.musicapp.presentation.presenters.TracksViewModel
import com.example.musicapp.presentation.presenters.factories.TracksVMFactory
import com.example.musicapp.presentation.ui.media.viewpager.TracksListUiState
import com.example.musicapp.presentation.ui.player.PlayerFragment
import com.example.musicapp.presentation.ui.trackAdapter.TrackAdapter
import javax.inject.Inject

class SinglePlaylistFragment : Fragment(R.layout.single_playlist_fragment),
    OnTrackClickListener {

    @Inject
    lateinit var vmFactory: TracksVMFactory
    private lateinit var vm: TracksViewModel
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        val mediaSubcomponent =
            requireActivity().applicationContext.component.mediaSubcomponent().create()
        mediaSubcomponent.inject(this)
        vm = ViewModelProvider(this, vmFactory)[TracksViewModel::class.java]
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emptyPlaylistMessage: LinearLayout =
            view.findViewById(R.id.ll_single_playlist_fragment_empty_playlist)

        recyclerView = view.findViewById(R.id.single_playlist_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Адаптер и ViewModel (observers)
        trackAdapter = TrackAdapter(this)
        recyclerView.adapter = trackAdapter

        vm.tracksList.observe(viewLifecycleOwner) {
            trackAdapter.updateList(it)
        }

        vm.uiState.observe(viewLifecycleOwner) {
            when (it) {
                TracksListUiState.Loading -> {}

                TracksListUiState.Success -> {}

                TracksListUiState.NoResults -> {
                    emptyPlaylistMessage.visibility = View.VISIBLE
                }

                else -> {
                    Log.e("uiState", "SinglePlaylistFragment: some uiState error")
                    Toast.makeText(
                        context,
                        "Непредвиденная ошибка: не удалось получить список треков",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        vm.getTracksList(this.requireArguments().getInt(ID_KEY))     // Получаем список треков

        requireView().findViewById<ImageButton>(R.id.single_playlist_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }

        super.onResume()
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

    override fun onItemClick(id: Long) {
        // Открытие PlayerFragment'а
        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putLong(TRACK_ID, id)
        bundle.putInt(PLAYLIST_ID, this.requireArguments().getInt(ID_KEY))
        playerFragment.arguments = bundle

        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.main_container, playerFragment)
            .addToBackStack("added PlayerFragment")
            .setReorderingAllowed(true)
            .commit()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private companion object {
        const val ID_KEY = "id key"
        const val TRACK_ID = "track id key"
        const val PLAYLIST_ID = "playlist id key"
    }
}