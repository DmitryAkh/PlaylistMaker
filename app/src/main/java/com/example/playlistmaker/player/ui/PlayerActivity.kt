package com.example.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.data.Track
import com.example.playlistmaker.util.Utils

class PlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    var track: Track? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            track = intent.getParcelableExtra(TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            track = intent.getParcelableExtra(TRACK)
        }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(
                MutableLiveData(),
                MutableLiveData(),
                Handler(Looper.getMainLooper()),
                Creator.providePlayerInteractor()
            )
        )[PlayerViewModel::class.java]
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareUI()
        setupObservers()


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
        viewModel.preparePlayer(track)

        Glide.with(this)
            .load(Utils.getCoverArtwork(track?.artworkUrl100))
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(8f, this)))
            .placeholder(R.drawable.placeholder312)
            .into(binding.cover)

        binding.artistName.text = track?.artistName
        binding.trackName.text = track?.trackName
        binding.trackTimeData.text = track?.trackTime
        binding.trackAlbumData.text = track?.collectionName
        binding.trackYearData.text = track?.releaseDate
        binding.trackGenreData.text = track?.primaryGenreName
        binding.trackCountryData.text = track?.country
        binding.playButton.setOnClickListener {
            viewModel.togglePlayer()
        }

    }

    private fun setupObservers() {
        viewModel.observeTime().observe(this) { time ->
            binding.playerTime.text = time
        }

        viewModel.observeState().observe(this) { state ->
            if (state == PlayerState.PLAYING) {
                binding.playButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayerActivity,
                        R.drawable.pause_button
                    )
                )
            } else {
                binding.playButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@PlayerActivity,
                        R.drawable.play_button
                    )
                )
            }
        }
    }

    companion object {
        private const val TRACK = "TRACK"
    }


}