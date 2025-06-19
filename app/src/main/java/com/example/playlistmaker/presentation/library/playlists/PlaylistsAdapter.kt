package com.example.playlistmaker.presentation.library.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.PlaylistItemBinding

class PlaylistsAdapter(
    private var plList: List<Playlist>,
    private var onPlaylistClicked: (Playlist) -> Unit,
) : RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {

        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaylistsViewHolder(binding)
    }

    override fun getItemCount(): Int = plList.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = plList[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlaylistClicked(playlist)
        }
    }

    fun updateData(newPlList: List<Playlist>) {
        this.plList = newPlList
        notifyDataSetChanged()
    }

}