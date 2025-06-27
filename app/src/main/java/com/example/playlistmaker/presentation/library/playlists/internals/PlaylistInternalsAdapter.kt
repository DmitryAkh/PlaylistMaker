package com.example.playlistmaker.presentation.library.playlists.internals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.entity.Track

class PlaylistInternalsAdapter(
    private var onTrackClicked: (Track) -> Unit,
    private var onTrackHold: (Track) -> Unit,
) : ListAdapter<Track, PlaylistInternalsViewHolder>(PlaylistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInternalsViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaylistInternalsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PlaylistInternalsViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClicked(track)
        }

        holder.itemView.setOnLongClickListener {
            onTrackHold(track)
            true
        }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}

