package com.example.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.domain.PlayerState.PLAYING
import com.example.playlistmaker.player.domain.PlayerState.PREPARED
import com.example.playlistmaker.player.domain.PlayerState.PAUSED
import com.example.playlistmaker.player.domain.PlayerState.DEFAULT
import com.example.playlistmaker.search.data.TRACK_FOR_PLAYER_KEY
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val sharedPrefs: SharedPreferences,
) : PlayerRepository {
    private var playerState = DEFAULT
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
            playerState = PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PAUSED
    }


    override fun release() = mediaPlayer.release()

    override fun getState(): PlayerState = playerState

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition


    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
            listener()
        }
    }

    override fun trackFromJson(json: String?): Track {
        val type = object : com.google.gson.reflect.TypeToken<Track>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun getTrack(): Track = track
}