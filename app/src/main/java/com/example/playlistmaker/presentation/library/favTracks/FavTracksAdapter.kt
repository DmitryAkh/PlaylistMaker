package com.example.playlistmaker.presentation.library.favTracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.entity.Track

class FavTracksAdapter(
    private var favTrackList: List<Track>,
    private var onTrackClicked: (Track) -> Unit,
) :
    RecyclerView.Adapter<FavTracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTracksViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavTracksViewHolder(binding, onTrackClicked)
    }

    override fun getItemCount(): Int = favTrackList.size

    override fun onBindViewHolder(holder: FavTracksViewHolder, position: Int) {
        val track = favTrackList[position]
        holder.bind(track)
    }

    fun updateData(newTrackList: List<Track>) {
        this.favTrackList = newTrackList
        notifyDataSetChanged()
    }
}