package com.example.playlistmaker.player.domain

import android.media.MediaPlayer
import com.example.playlistmaker.search.domain.Track

interface PlayerRepository {
    fun getTrackFromSharedPrefs(): Track
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getState(): PlayerState
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun trackFromJson(json: String?): Track
    fun getTrack(): Track
    fun getPlayer(): MediaPlayer
    fun getStringPlayerPosition(): String
}