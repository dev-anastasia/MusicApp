package com.example.musicapp.presentation.ui.media.playlistsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.database.PlaylistEntity

class DiffUtilPlaylistItemCallback : DiffUtil.ItemCallback<PlaylistEntity>() {
    override fun areItemsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity): Boolean {
        return (oldItem.playlistId == newItem.playlistId)
    }

    override fun areContentsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity): Boolean {
        return (oldItem.cover == newItem.cover
                &&
                oldItem.name == newItem.name
                &&
                oldItem.songCount == newItem.songCount)
    }
}