package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.search.domain.models.Track

class HistoryAdapter(
    private var historyList: List<Track>,
    private var onTrackClicked: (Track) -> Unit,
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrackViewHolder(binding, onTrackClicked)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = historyList[position]
        holder.bind(track)
    }

    fun updateData(newHistoryList: List<Track>) {
        this.historyList = newHistoryList
        notifyDataSetChanged()
    }
}