package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding

class HistoryAdapter(
    private val historyList: List<Track>,
    private var searchHistory: SearchHistory,
) :
    RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrackViewHolder(binding, searchHistory)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = historyList[position]
        holder.bind(track)
    }
}