package com.example.playlistmaker.presentation.player

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var track: Track


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        track = viewModel.getTrack()
        prepareUI()
        setupObservers()
        viewModel.syncFavorite()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }


    private fun prepareUI() {
        binding.backButton.setOnClickListener {
            finish()
        }
        viewModel.preparePlayer()

        Glide.with(this)
            .load(Utils.getCoverArtwork(track.artworkUrl100))
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(8f, this)))
            .placeholder(R.drawable.placeholder312)
            .into(binding.cover)

        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.trackTimeData.text = track.trackTime
        binding.trackAlbumData.text = track.collectionName
        binding.trackYearData.text = track.releaseDate
        binding.trackGenreData.text = track.primaryGenreName
        binding.trackCountryData.text = track.country
        binding.playButton.setOnClickListener {
            viewModel.togglePlayer()
        }

        binding.likeButton.setOnClickListener {
            if (viewModel.isFavorite()) {
                track.isFavorite = false
                viewModel.deleteFavTrack(track.trackId)
            } else {
                track.isFavorite = true
                viewModel.addFavTrack(track)
            }
            viewModel.syncFavorite()
        }




    }

    private fun setupObservers() {

        viewModel.observeTime().observe(this) { time ->
            binding.playerTime.text = time
        }

        viewModel.observeState().observe(this) { state ->
            val drawableRes = if (state == PlayerState.PLAYING) {
                R.drawable.pause_button
            } else {
                R.drawable.play_button
            }

            binding.playButton.setImageDrawable(
                ContextCompat.getDrawable(this, drawableRes)
            )
        }

        viewModel.observeIsFavorite().observe(this) { isFavorite ->
            val drawableRes = if (isFavorite == true) {
                R.drawable.like_button_active
            } else {
                R.drawable.like_button_inactive
            }

            binding.likeButton.setImageDrawable(
                ContextCompat.getDrawable(this, drawableRes)
            )
        }
    }


}