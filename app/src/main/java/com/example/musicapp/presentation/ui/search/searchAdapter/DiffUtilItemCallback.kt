package com.example.musicapp.presentation.ui.search.searchAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.musicapp.domain.entities.MusicTrack

// (Пока что) не используется

class DiffUtilItemCallback : DiffUtil.ItemCallback<MusicTrack>() {
    override fun areItemsTheSame(oldItem: MusicTrack, newItem: MusicTrack): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: MusicTrack, newItem: MusicTrack): Boolean {
        TODO("Not yet implemented")
    }

}

class MusicListAdapter() : ListAdapter<MusicTrack, MusicPieceViewHolder>(DiffUtilItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}
