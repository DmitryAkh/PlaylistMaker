package com.example.playlistmaker.domain.interactors

import android.media.MediaPlayer
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.domain.entity.Track


interface PlayerInteractor {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getState(): PlayerState
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun getTrack(): Track
    fun getPlayer(): MediaPlayer
    fun isFavorite(): Boolean
}