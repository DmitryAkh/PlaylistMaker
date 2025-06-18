package com.example.playlistmaker.presentation.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
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
            .placeholder(R.drawable.placeholder)
            .into((binding.cover))



        binding.name.text = item.playlistName
        binding.count.text = binding.root.resources.getQuantityString(
            R.plurals.tracks_count,
            item.tracksIds.size,
            item.tracksIds.size
        )
    }

}
