package com.example.playlistmaker

import android.media.MediaPlayer
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
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class Player : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var trackUrl: String? = null
    private val handler = Handler(Looper.getMainLooper())

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            binding.playerTime.text = Utils.formatTrackTime(mediaPlayer.currentPosition.toLong())
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val track: Track?


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            track = intent.getParcelableExtra("TRACK", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            track = intent.getParcelableExtra("TRACK")
        }

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        Glide.with(this)
            .load(track?.getCoverArtwork())
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

        trackUrl = track?.previewUrl

        preparePlayer()

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
        mediaPlayer.release()
    }

    override fun onResume() {
        super.onResume()
        if (playerState == STATE_PLAYING) {
            handler.post(updateTimerRunnable)
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this@Player,
                    R.drawable.play_button
                )
            )
            playerState = STATE_PREPARED
            binding.playerTime.text = getString(R.string.default_timer)
            handler.removeCallbacks(updateTimerRunnable)
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageDrawable(
            ContextCompat.getDrawable(
                this@Player,
                R.drawable.pause_button
            )
        )
        playerState = STATE_PLAYING
        handler.post(updateTimerRunnable)

    }

    fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageDrawable(
            ContextCompat.getDrawable(
                this@Player,
                R.drawable.play_button
            )
        )
        playerState = STATE_PAUSED
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }

    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}
