package com.example.playlistmaker.presentation.library.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.util.Utils

class PlaylistsViewHolder(
    private val binding: PlaylistItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {

        Glide.with(itemView.context)
            .load(item.coverPath)
            .centerCrop()
            .transform((RoundedCorners(Utils.dpToPx(8f, itemView.context))))
            .placeholder(R.drawable.placeholder)
            .into((binding.cover))

        val playlistSize = item.tracksIds.size

        binding.name.text = item.playlistName
        binding.count.text = playlistSize.toString()

    }

}