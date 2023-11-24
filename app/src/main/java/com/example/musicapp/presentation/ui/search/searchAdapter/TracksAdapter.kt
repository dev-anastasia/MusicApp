package com.example.musicapp.presentation.ui.search.searchAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.entities.MusicTrack
import com.example.musicapp.presentation.OnTrackClickListener
import com.squareup.picasso.Picasso

class TracksAdapter(
    private val itemIdListener: OnTrackClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<MusicPieceViewHolder>() {

    private val list: MutableList<MusicTrack> = mutableListOf()

    fun getList(): MutableList<MusicTrack> {
        return this.list
    }

    fun updateList(newList: List<MusicTrack>) {
        val diffUtil = DiffUtil.calculateDiff(
            MusicPieceDiffUtilCallback(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item_view, parent, false)
        return MusicPieceViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        holder.songName.text = list[position].trackName
        holder.author.text = list[position].artistName
        Picasso.get()
            .load(list[position].artworkUrl60)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            onClick(list[position].trackId)
        }
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
                    (MusicPieceDiffUtilCallback.NAME) ->
                        holder.updateAuthorName(bundle.getString(key)!!)

                    (MusicPieceDiffUtilCallback.TRACK) ->
                        holder.updateTrackName(bundle.getString(key)!!)

                    (MusicPieceDiffUtilCallback.COVER) ->
                        holder.updateCoverImage(bundle.getString(key)!!)
                }
            }
        }
    }
}
