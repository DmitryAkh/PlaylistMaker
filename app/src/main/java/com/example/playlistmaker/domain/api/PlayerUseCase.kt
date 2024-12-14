package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track


interface PlayerUseCase {
    fun preparePlayer(track: Track?)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getState(): Int
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
}