package com.example.musicapp.presentation.ui.media.playlistsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.database.PlaylistTable

class DiffUtilPlaylistItemCallback : DiffUtil.ItemCallback<PlaylistTable>() {
    override fun areItemsTheSame(oldItem: PlaylistTable, newItem: PlaylistTable): Boolean {
        return (oldItem.playlistId == newItem.playlistId)
    }

    override fun areContentsTheSame(oldItem: PlaylistTable, newItem: PlaylistTable): Boolean {
        return (oldItem.playlistCover == newItem.playlistCover
                &&
                oldItem.playlistName == newItem.playlistName)
    }
}