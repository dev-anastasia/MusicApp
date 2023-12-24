package com.example.musicapp.presentation.ui.playlistAdapter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.musicapp.R
import com.example.musicapp.domain.entities.Playlist
import com.example.musicapp.presentation.OnPlaylistClickListener

class PlaylistAdapter(
    private val itemIdListener: OnPlaylistClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<PlaylistViewHolder>(),
    PopupMenu.OnMenuItemClickListener {

    private val list: MutableList<Playlist> = mutableListOf()
    private var currPlaylistId = 0
    private var currPlaylistPosition = 0
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        // Создаём по макету из layout'а холдер для наших вьюшек
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.media_playlists_item_view, parent, false)
        return PlaylistViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        bindWithDatabase(holder, holder.adapterPosition)

        holder.itemView.setOnClickListener {
            onPlaylistClick(list[holder.adapterPosition].playlistId)
        }

        holder.menu.setOnClickListener {
            currPlaylistId = list[holder.adapterPosition].playlistId
            currPlaylistPosition = holder.adapterPosition
            println(currPlaylistId)
            showMenu(holder.menu)
        }
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, holder.adapterPosition, payloads)
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

    private fun bindWithDatabase(holder: PlaylistViewHolder, position: Int) {
        holder.name.text = list[holder.adapterPosition].playlistName

        itemIdListener.getPlaylistInfo(list[holder.adapterPosition].playlistId) { info ->
            mainHandler.post {
                holder.updatePlaylistCoverImage(info.cover)
                holder.updateTracksCount(info.tracksCount)
            }
        }
    }

    private fun onPlaylistClick(id: Int) {
        itemIdListener.openPlaylistClicked(id)
    }

    // Методы для PopupMenu:

    private fun showMenu(view: View) {
        val pMenu = PopupMenu(view.context, view)
        pMenu.apply {
            inflate(R.menu.menu)
            pMenu.menu.add(R.string.context_menu_delete)
            setOnMenuItemClickListener(this@PlaylistAdapter)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        itemIdListener.deletePlaylistClicked(currPlaylistId)
        return true
    }
}
