package com.example.musicapp.presentation.ui.media.tracksAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.database.TrackTable
import com.example.musicapp.presentation.OnTrackClickListener
import com.squareup.picasso.Picasso

class TrackEntityAdapter(
    private val itemIdListener: OnTrackClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<TrackEntityViewHolder>() {

    private val list: MutableList<TrackTable> = mutableListOf()

    fun updateList(newList: List<TrackTable>) {
        val diffUtil = DiffUtil.calculateDiff(
            TrackEntityDiffUtilCallback(
                list,
                newList
            )
        )
        list.clear()
        list.addAll(newList)
        diffUtil.dispatchUpdatesTo(this)
    }

    private fun onClick(id: Long) {
        itemIdListener.onItemClick(id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackEntityViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item_view, parent, false)
        return TrackEntityViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TrackEntityViewHolder, position: Int) {
        holder.songName.text = list[position].trackName
        holder.author.text = list[position].artistName
        Picasso.get()
            .load(list[position].artworkUrl60)
            .placeholder(R.drawable.note_placeholder)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            onClick(list[position].trackId)
        }
    }

    override fun onBindViewHolder(
        holder: TrackEntityViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val bundle = payloads.first() as Bundle
            for (key in bundle.keySet()) {
                when (key) {
                    (TrackEntityDiffUtilCallback.NAME) ->
                        holder.updateAuthorName(bundle.getString(key)!!)

                    (TrackEntityDiffUtilCallback.TRACK) ->
                        holder.updateTrackName(bundle.getString(key)!!)

                    (TrackEntityDiffUtilCallback.COVER) ->
                        holder.updateCoverImage(bundle.getString(key)!!)
                }
            }
        }
    }
}
