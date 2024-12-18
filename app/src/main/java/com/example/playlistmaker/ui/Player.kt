package com.example.playlistmaker.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.Utils
import com.example.playlistmaker.Utils.getCoverArtwork
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.PlayerState.PLAYING
import com.example.playlistmaker.domain.models.PlayerState.PREPARED
import com.example.playlistmaker.domain.models.PlayerState.PAUSED

private const val TRACK = "TRACK"

class Player : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var interactor: PlayerInteractor
    private val handler = Handler(Looper.getMainLooper())

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            binding.playerTime.text =
                Utils.formatTrackTime(interactor.getCurrentPosition().toLong())
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val track: Track?


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            track = intent.getParcelableExtra(TRACK, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            track = intent.getParcelableExtra(TRACK)
        }

        interactor = Creator.providePlayerInteractor()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(getCoverArtwork(track?.artworkUrl100))
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


        preparePlayer(track)

        binding.playButton.setOnClickListener {
            playbackControl()
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacks(updateTimerRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimerRunnable)
        interactor.release()
    }

    override fun onResume() {
        super.onResume()
        if (interactor.getState() == PLAYING) {
            handler.post(updateTimerRunnable)
        }
    }

    private fun preparePlayer(track: Track?) {
        interactor.preparePlayer(track)
        interactor.setOnCompletionListener {
            binding.playButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this@Player,
                    R.drawable.play_button
                )
            )
        }
        binding.playerTime.text = getString(R.string.default_timer)
        handler.removeCallbacks(updateTimerRunnable)

    }

    private fun startPlayer() {
        interactor.startPlayer()
        binding.playButton.setImageDrawable(
            ContextCompat.getDrawable(
                this@Player,
                R.drawable.pause_button
            )
        )
        handler.post(updateTimerRunnable)

    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        binding.playButton.setImageDrawable(
            ContextCompat.getDrawable(
                this@Player,
                R.drawable.play_button
            )
        )
    }

    private fun playbackControl() {
        if (interactor.getState() == PLAYING) {

                pausePlayer()
        } else if (interactor.getState() == PREPARED || interactor.getState() == PAUSED)

                startPlayer()
            }


}



