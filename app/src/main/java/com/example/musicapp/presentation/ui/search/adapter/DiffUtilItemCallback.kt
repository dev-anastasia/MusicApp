package com.example.musicapp.presentation.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.musicapp.domain.entities.MusicPiece

// (Пока что) не используется

class DiffUtilItemCallback : DiffUtil.ItemCallback<MusicPiece>() {
    override fun areItemsTheSame(oldItem: MusicPiece, newItem: MusicPiece): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: MusicPiece, newItem: MusicPiece): Boolean {
        TODO("Not yet implemented")
    }

}

class MusicListAdapter() : ListAdapter<MusicPiece, MusicPieceViewHolder>(DiffUtilItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}
