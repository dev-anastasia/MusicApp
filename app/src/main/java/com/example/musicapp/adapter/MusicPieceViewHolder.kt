package com.example.musicapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R

class MusicPieceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cover: ImageView = itemView.findViewById(R.id.item_view_iv_cover)
    val songName: TextView = itemView.findViewById(R.id.tv_song_name)
    val author: TextView = itemView.findViewById(R.id.tv_author)
}