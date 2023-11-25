package com.example.musicapp.presentation.ui.playlistAdapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.squareup.picasso.Picasso

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val cover: ImageView = itemView.findViewById(R.id.playlist_item_view_iv_cover)
    val name: TextView = itemView.findViewById(R.id.playlist_item_view_tv_album_name)
    val count: TextView = itemView.findViewById(R.id.playlist_item_view_tv_tracks_count)
    val menu: ImageButton = itemView.findViewById(R.id.playlists_item_view_context_menu)

    fun updatePlaylistName(name: String) {
        this.name.text = name
    }

    fun updateTracksCount(count: Int) {
        this.count.text = count.toString()
    }

    fun updatePlaylistCoverImage(link: String) {
        Picasso.get()
            .load(link)
            .placeholder(R.drawable.note_placeholder)
            .into(cover)
    }
}