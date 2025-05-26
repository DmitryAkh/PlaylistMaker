package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.entity.Track

class SearchAdapter(
    private var trackList: List<Track>,
    private var onTrackClicked: (Track) -> Unit,
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchViewHolder(binding, onTrackClicked)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
    }

    fun updateData(newTrackList: List<Track>) {
        this.trackList = newTrackList
        notifyDataSetChanged()
    }
}