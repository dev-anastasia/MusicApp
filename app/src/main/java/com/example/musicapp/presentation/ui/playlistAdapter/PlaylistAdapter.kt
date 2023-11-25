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
import com.squareup.picasso.Picasso

class PlaylistAdapter(
    private val itemIdListener: OnPlaylistClickListener    // Интерфейс для выбора item'а из RV
) : Adapter<PlaylistViewHolder>(),
    PopupMenu.OnMenuItemClickListener {

    private val list: MutableList<Playlist> = mutableListOf()
    private var currPlaylistId = 0
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

        bindWithDatabase(holder, position)

        holder.name.text = list[position].playlistName

        holder.itemView.setOnClickListener {
            onPlaylistClick(list[position].playlistId)
        }

        holder.menu.setOnClickListener {
            currPlaylistId = list[position].playlistId
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
        notifyDataSetChanged()  // Хотела использовать NotifyItemInserted, но не могу правильно передать в него position
    }

    private fun bindWithDatabase(holder: PlaylistViewHolder, position: Int) {

        Thread {
            itemIdListener.getPlaylistCover(list[position].playlistId) { str ->
                mainHandler.post {
                    Picasso.get()
                        .load(str)
                        .placeholder(R.drawable.note_placeholder)
                        .into(holder.cover)
                }
            }
        }.start()

        Thread {
            itemIdListener.getPlaylistTracksCount(list[position].playlistId) { count ->
                mainHandler.post {
                    holder.count.text = "Всего треков: $count"
                }
            }
        }.start()
    }

    private fun onPlaylistClick(id: Int) {
        itemIdListener.openPlaylistClicked(id)
    }

    // Методы для PopupMenu:

    private fun showMenu(view: View) {
        val pMenu = PopupMenu(view.context, view)
        pMenu.apply {
            inflate(R.menu.menu)
            pMenu.menu.add("Удалить")
            setOnMenuItemClickListener(this@PlaylistAdapter)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        itemIdListener.deletePlaylistClicked(currPlaylistId)
        return true
    }
}
