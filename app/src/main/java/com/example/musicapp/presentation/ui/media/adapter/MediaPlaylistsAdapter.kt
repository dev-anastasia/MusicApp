package com.example.musicapp.presentation.ui.media.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.entities.database.PlaylistEntity
import com.example.musicapp.presentation.OnPlaylistClickListener
import com.squareup.picasso.Picasso

class MediaPlaylistsAdapter(
    private val itemIdListener: OnPlaylistClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<PlaylistViewHolder>(),
    PopupMenu.OnMenuItemClickListener {

    private val list: MutableList<PlaylistEntity> = mutableListOf()


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
            .placeholder(R.drawable.note_placeholder)
            .into(holder.cover)

        holder.itemView.setOnClickListener {
            onPlaylistClick(list[position].id)
        }

        holder.menu.setOnClickListener {
            itemIdListener.currId = position
            showMenu(holder.menu)
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

    fun updateList(newList: List<PlaylistEntity>) {
        val diffUtil = DiffUtil.calculateDiff(
            PlaylistDiffUtilCallback(
                list,
                newList
            )
        )
        list.clear()
        list.addAll(newList)
        diffUtil.dispatchUpdatesTo(this)
        notifyItemInserted(0)
    }

    private fun onPlaylistClick(id: Int) {
        itemIdListener.openPlaylistClicked(id)
    }

    // Ниже - методы для PopupMenu

    private fun showMenu(view: View) {
        val pMenu = PopupMenu(view.context, view)
        pMenu.apply {
            inflate(R.menu.menu)
            setOnMenuItemClickListener(this@MediaPlaylistsAdapter)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        itemIdListener.deletePlaylistClicked()
        return true
    }
}
