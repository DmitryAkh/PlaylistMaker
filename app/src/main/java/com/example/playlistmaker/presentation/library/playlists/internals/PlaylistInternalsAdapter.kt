package com.example.playlistmaker.presentation.library.playlists.internals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.entity.Track

class PlaylistInternalsAdapter(
    private var playlist: List<Track>,
    private var onTrackClicked: (Track) -> Unit,
    private var onTrackHold: (Track) -> Unit,
) : RecyclerView.Adapter<PlaylistInternalsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistInternalsViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaylistInternalsViewHolder(binding)
    }

    override fun getItemCount(): Int = playlist.size

    override fun onBindViewHolder(holder: PlaylistInternalsViewHolder, position: Int) {
        val track = playlist[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClicked(track)
        }

        holder.itemView.setOnLongClickListener {
            onTrackHold(track)
            true
        }
    }

    fun updateData(newTrackList: List<Track>) {
        playlist = newTrackList
        notifyDataSetChanged()
    }
}