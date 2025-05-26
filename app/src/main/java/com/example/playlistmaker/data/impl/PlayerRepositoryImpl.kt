package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.domain.repositories.PlayerRepository
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.data.impl.SearchRepositoryImpl.Companion.TRACK_FOR_PLAYER_KEY
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val sharedPrefs: SharedPreferences,
) : PlayerRepository {
    private var playerState: PlayerState = PlayerState.DEFAULT
    private var track = getTrackFromSharedPrefs()


    override fun getTrackFromSharedPrefs(): Track {
        val json = sharedPrefs.getString(TRACK_FOR_PLAYER_KEY, null)
        return trackFromJson(json)
    }

    override fun preparePlayer() {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }


    override fun release() = mediaPlayer.release()

    override fun getState(): PlayerState = playerState

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun getStringPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(getCurrentPosition())
            ?: "00:00"
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            listener()
        }
    }

    override fun trackFromJson(json: String?): Track {
        val type = object : com.google.gson.reflect.TypeToken<Track>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun getTrack(): Track = track

    override fun getPlayer(): MediaPlayer = mediaPlayer

    override fun isFavorite(): Boolean {
        return track.isFavorite
    }
}