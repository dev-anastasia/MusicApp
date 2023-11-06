package com.example.musicapp.presentation.ui.media.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.entities.room.Playlist
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.squareup.picasso.Picasso

class MediaPlaylistsAdapter(
    private val itemIdListener: OnPlaylistClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<PlaylistViewHolder>() {

    private val list: MutableList<Playlist> = mutableListOf()

    fun updateList(newList: List<Playlist>) {
        val diffUtil = DiffUtil.calculateDiff(
            PlaylistDiffUtilCallback(
                list,
                newList
            )
        )
        list.clear()
        list.addAll(newList)
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun onClick(id: Int) {
        itemIdListener.onPlaylistClick(id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.media_playlists_item_view, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        holder.name.text = list[position].name
        holder.count.text = list[position].songCount.toString()

        Picasso.get()
            .load(list[position].cover)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            onClick(list[position].id)
        }
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads.first() as Bundle
            for (key in bundle.keySet()) {
                when (key) {
                    (PlaylistDiffUtilCallback.NAME) ->
                        holder.updatePlaylistName(bundle.getString(key)!!)

                    (PlaylistDiffUtilCallback.COUNT) ->
                        holder.updateTracksCount(bundle.getInt(key))

                    (PlaylistDiffUtilCallback.COVER) ->
                        holder.updatePlaylistCoverImage(bundle.getString(key)!!)
                }
            }
        }
    }
}
