package com.example.musicapp.presentation.ui.media.playlistsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.musicapp.domain.database.PlaylistTable
import com.example.musicapp.domain.entities.Playlist

class DiffUtilPlaylistItemCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return (oldItem.playlistId == newItem.playlistId)
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return (oldItem.playlistCover == newItem.playlistCover
                &&
                oldItem.playlistName == newItem.playlistName)
    }
}