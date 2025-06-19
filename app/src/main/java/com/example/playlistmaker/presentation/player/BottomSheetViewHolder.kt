package com.example.playlistmaker.presentation.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R.*
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.PlaylistItemBottomSheetBinding
import com.example.playlistmaker.util.Utils


class BottomSheetViewHolder(private val binding: PlaylistItemBottomSheetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Playlist) {

        Glide.with(itemView.context)
            .load(item.coverPath)
            .centerCrop()
            .transform((RoundedCorners(Utils.dpToPx(8f, itemView.context))))
            .placeholder(drawable.placeholder)
            .into((binding.cover))

        val tracksCount = item.tracksIds.size
        val trackCountEnding = Utils.tracksCountEnding(tracksCount)

        binding.name.text = item.playlistName
        binding.count.text =
            itemView.context.getString(string.track_count, tracksCount, trackCountEnding)

    }

}
