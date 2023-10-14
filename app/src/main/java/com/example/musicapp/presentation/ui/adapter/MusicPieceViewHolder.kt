package com.example.musicapp.presentation.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.squareup.picasso.Picasso

class MusicPieceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cover: ImageView = itemView.findViewById(R.id.item_view_iv_cover)
    val songName: TextView = itemView.findViewById(R.id.tv_song_name)
    val author: TextView = itemView.findViewById(R.id.tv_author)

    fun updateAuthorName(name: String) {
        author.text = name
    }

    fun updateTrackName(name: String) {
        songName.text = name
    }

    fun updateCoverImage(link: String) {
        Picasso.get()
            .load(link)
            .placeholder(R.drawable.note_placeholder)
            .into(cover)
    }
}