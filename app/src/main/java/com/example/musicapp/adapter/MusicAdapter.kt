package com.example.musicapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.PlayerActivity
import com.example.musicapp.R
import com.example.musicapp.SearchActivity.Companion.ARTIST_NAME_KEY
import com.example.musicapp.SearchActivity.Companion.DURATION_KEY
import com.example.musicapp.SearchActivity.Companion.SONG_NAME_KEY
import com.example.musicapp.SearchActivity.Companion.TRACK_COVER_KEY
import com.example.musicapp.SearchActivity.Companion.TRACK_LINK_KEY
import com.example.musicapp.data.MusicPiece
import com.squareup.picasso.Picasso

class MusicAdapter(var list: List<MusicPiece>, private val context: Context) :
    Adapter<MusicPieceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return MusicPieceViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    private fun onClick(holder: MusicPieceViewHolder, position: Int) {
        holder.itemView.setOnClickListener {

            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_COVER_KEY, list[position].artworkUrl60)
            intent.putExtra(SONG_NAME_KEY, list[position].trackName)
            intent.putExtra(ARTIST_NAME_KEY, list[position].artistName)
            intent.putExtra(DURATION_KEY, list[position].trackTimeMillis)
            intent.putExtra(TRACK_LINK_KEY, list[position].previewUrl)

            context.startActivity(intent)
        }
    }

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        holder.songName.text = list[position].trackName
        holder.author.text = list[position].artistName
        Picasso.get()
            .load(list[position].artworkUrl60)
            .into(holder.cover)

        onClick(holder, position)   // вызывать в onBindViewHolder или лучше куда-либо переместить?
    }

    override fun onBindViewHolder(
        holder: MusicPieceViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads.first() as Bundle
            for (key in bundle.keySet()) {
                when (key) {
                    (ItemDiffUtilCallback.NAME) ->
                        holder.updateAuthorName(bundle.getString(key)!!)

                    (ItemDiffUtilCallback.TRACK) ->
                        holder.updateTrackName(bundle.getString(key)!!)

                    (ItemDiffUtilCallback.COVER) ->
                        holder.updateCoverImage(bundle.getString(key)!!)
                }
            }
        }
    }
}