package com.example.playlistmaker.presentation.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding


class BottomSheetAdapter(
    private var plList: List<Playlist>,
    private var onPlaylistClicked: (Playlist) -> Unit,
) : RecyclerView.Adapter<BottomSheetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {

        val binding = PlaylistItemBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BottomSheetViewHolder(binding)
    }


    override fun getItemCount(): Int = plList.size

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
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