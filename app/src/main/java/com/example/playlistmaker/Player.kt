package com.example.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding

class Player : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val track = intent.getParcelableExtra<Track>("TRACK")

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




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}