package com.example.playlistmaker



import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackItemBinding

class TrackViewHolder(
    private val binding: TrackItemBinding, private val searchHistory: SearchHistory,
) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Track) {

        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(2f, itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(binding.ivImgTrack)


        binding.tvTrackName.text = item.trackName
        binding.tvArtistName.text = item.artistName
        binding.tvTrackTime.text = item.trackTime

        binding.root.setOnClickListener() {
            searchHistory.addTrackToHistory(item)
        }


    }

}