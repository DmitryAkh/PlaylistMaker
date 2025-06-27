package com.example.playlistmaker.presentation.library.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.PlaylistItemBinding

class PlaylistsAdapter(
    private var onPlaylistClicked: (Playlist) -> Unit,
) : ListAdapter<Playlist, PlaylistsViewHolder>(PlaylistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {

        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onPlaylistClicked(playlist)
        }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.playlistId == newItem.playlistId
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }

    }
}