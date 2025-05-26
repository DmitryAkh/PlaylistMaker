package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.Track


interface PlayerInteractor {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getState(): PlayerState
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
    fun getTrack(): Track
}