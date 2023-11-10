package com.example.musicapp.presentation.ui.media.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.entities.database.PlaylistEntity

class DiffUtilPlaylistItemCallback : DiffUtil.ItemCallback<PlaylistEntity>() {
    override fun areItemsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity): Boolean {
        return (oldItem.cover == newItem.cover
                &&
                oldItem.name == newItem.name
                &&
                oldItem.songCount == newItem.songCount
                &&
                oldItem.timeMillis == newItem.timeMillis)
    }
}