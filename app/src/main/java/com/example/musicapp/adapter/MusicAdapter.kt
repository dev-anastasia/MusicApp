package com.example.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.data.MusicPiece
import com.squareup.picasso.Picasso

class MusicAdapter(private val list: List<MusicPiece>) : Adapter<MusicPieceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return MusicPieceViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        holder.songName.text = list[position].trackName
        holder.author.text = list[position].artistName
        Picasso.get()
            .load(list[position].artworkUrl60)
            .into(holder.cover)
    }
}