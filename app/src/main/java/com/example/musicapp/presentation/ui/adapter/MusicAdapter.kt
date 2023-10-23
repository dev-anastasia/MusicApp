package com.example.musicapp.presentation.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.entities.Music
import com.example.musicapp.domain.entities.MusicPiece
import com.example.musicapp.presentation.OnItemClickListener
import com.squareup.picasso.Picasso

class DiffUtilItemCallback: DiffUtil.ItemCallback<MusicPiece>(){
    override fun areItemsTheSame(oldItem: MusicPiece, newItem: MusicPiece): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: MusicPiece, newItem: MusicPiece): Boolean {
        TODO("Not yet implemented")
    }

}

class MusicListAdapter(): ListAdapter<MusicPiece, MusicPieceViewHolder>(DiffUtilItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPieceViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MusicPieceViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}

class MusicAdapter(
    private val itemIdListener: OnItemClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<MusicPieceViewHolder>() {

    private val list: MutableList<MusicPiece> = mutableListOf()

    fun updateList(newList: List<MusicPiece>) {
        val diffUtil = DiffUtil.calculateDiff(
            ItemDiffUtilCallback(
                list,
                newList
            )
        )
        list.clear()
        list.addAll(newList)
        diffUtil.dispatchUpdatesTo(this)
    }

//    fun getList(): List<MusicPiece> {
//        return list
//    }

    private fun onClick(id: Long) {
        itemIdListener.onItemClick(id)
    }

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
