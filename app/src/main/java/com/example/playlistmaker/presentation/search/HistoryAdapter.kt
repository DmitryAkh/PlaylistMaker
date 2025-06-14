package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.entity.Track

class HistoryAdapter(
    private var historyList: List<Track>,
    private var onTrackClicked: (Track) -> Unit,
) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val track = historyList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClicked(track)
        }
    }

    fun updateData(newHistoryList: List<Track>) {
        this.historyList = newHistoryList
        notifyDataSetChanged()
    }
}